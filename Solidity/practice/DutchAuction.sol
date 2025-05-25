// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
/**
 * 荷兰拍卖，对某个nft进行拍卖
 * 部署步骤：
 * 0、先部署ERC721.sol合约
 * 1、在ERC721合约中调用mint方法，进行铸造
 * 1、部署 DutchAuction合约
 * 2、将DutchAuction的合约地址传入ERC721合约中调用approve方法，进行授权。
 * 3、切换其他账户并调用 DutchAuction合约的buy函数，进行购买
 * 3、调用 DutchAuction合约的getPrice函数，获取当前价格
 * 4、调用 DutchAuction合约的selfdestruct函数，销毁合约
 * 5、调用 DutchAuction合约的payable函数，支付价格
 * 6、调用 DutchAuction合约的transferFrom函数，发送nft
 */
interface IERC721{
    function transferFrom(
        address _from,
        address _to,
        uint _nftId
    ) external;
}

contract DutchAuction{
    //拍卖的有效时间是7天
    uint private constant DURATION = 7 days;
    //nft的合约地址
    IERC721 public immutable nft;
    // nftId
    uint public immutable nftId;
    //当前持有者的地址
    address payable public immutable seller;
    //起拍价格
    uint public immutable startingPrice;
    //拍卖开始时间
    uint public immutable startAt;
    //拍卖结束时间
    uint public immutable expiresAt;
    //每秒的折扣率，因为每秒价格都会降低
    uint public immutable discountRate;

    constructor(
        uint _startingPrice,
        uint _discountRate,
        address _nft, //nft的合约地址
        uint _nftId
    ) {
        //持有者地址
        seller = payable(msg.sender);
        //起拍价格
        startingPrice = _startingPrice;
        //折扣率
        discountRate = _discountRate;
        startAt = block.timestamp;
        expiresAt = block.timestamp + DURATION;
        require(
            _startingPrice >= _discountRate * DURATION,
            "starting price < discount");
        //nft地址值
        nft = IERC721(_nft);
        //nftId
        nftId = _nftId;
    }

    function getPrice() public view returns(uint){
        //计算流逝的时间：当前时间-开始时间
        uint timeElapsed = block.timestamp - startAt;
        //折扣金额 = 折扣率 * 流逝的时间
        uint discount = discountRate * timeElapsed;
        //当前价格=起拍价格-折扣价格
        return startingPrice-discount;
    }

    function buy() external payable {
        //判断当前时间必须小于过期时间
        require(block.timestamp <expiresAt,"auction expirced");
        //获取当前价格
        uint price = getPrice();
        //合约调用者的余额必须大于nft的当前价格
        require(msg.value >= price,"ETH < price");
        //发送nft，从持有者地址给当前调用者地址，nftId
        nft.transferFrom(seller, msg.sender, nftId);
        //当前调用者的余额-当前价格
        uint refund = msg.value - price;
        //如果大于0
        if(refund > 0){
            //将多出的gas返还给msg.sender
            payable(msg.sender).transfer(refund);
        }
        //合约自毁
        selfdestruct(seller);
    }
}