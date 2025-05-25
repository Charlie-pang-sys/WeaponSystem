// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import {AggregatorV3Interface} from "@chainlink/contracts/src/v0.8/shared/interfaces/AggregatorV3Interface.sol";
//1、创建一个收款函数
//2、记录投资人并且查看
//3、在锁定期内，达到目标值，生产商可以提款
//4、在锁定期内，没有达到目标值，投资人在锁定期以后退款
contract FundMe{
    AggregatorV3Interface internal dataFeed;
    //投资人
    mapping(address =>uint256) public fundersToAmount;
    address[] public funders;
    event EthToUsdEvent(string message ,uint256 money);
    event MoneySumEvent(string message, uint256 money);
    //最小额度
    uint256 MININUM_VALUE = 100 * 10 **18; //usd
    //关于TARGET为什么是1000 * 10 ** 18，因为合约中默认的金额但是为wei，此处的TARGET是wei的USD
    uint256 constant TARGET = 300 * 10 ** 18; //目标值是1000
    address public owner;
    constructor(){
        owner = msg.sender;
        //Sepolia Testnet的USD-ETH的地址
        dataFeed = AggregatorV3Interface(0x694AA1769357215DE4FAC081bf1f309aDC325306);
    }

    function fund() external payable {
        //fundersToAmount[msg.sender]  = fundersToAmount[msg.sender]+msg.value;
        //限定最小额度
        uint256 money = convertEthToUsd(msg.value);
        emit EthToUsdEvent("convert monry is:",money);
        /**
        * 下面这一行代码等同于
        *uint256 convertToRealMoney = money/(10 **18);
        *require(money >= 100,"send more ETH");
         */
        require(money >= MININUM_VALUE,"send more ETH");
        fundersToAmount[msg.sender] = msg.value;
        funders.push(msg.sender);
    }
    
    function getBalanceOf() public view returns (uint){
        return address(this).balance;
    }

    function getChainLinkDataFeedLatestAnswer() public view returns (int){
        //prettier-ignore
        (
            /*uint80 roundID */,
            int answer,
            /*uint startedAt*/,
            /*uint timeStamp*/,
            /*uint80 answeredInRound*/
        ) = dataFeed.latestRoundData();
        return answer;
    }
    //返回wei对应的usd
    function convertEthToUsd(uint256 ethAmount) internal view returns(uint256){
        //此处的ethAmount单位是wei，这块不能用ethAmount/(10 **18)，有可能是小数
        uint256 ethPrice = uint256(getChainLinkDataFeedLatestAnswer());
        return ethAmount * ethPrice / (10 **8);
    }
    /**
    * @dev 提款功能
     */
    function getFund() external{
        uint256 moneySum = convertEthToUsd(address(this).balance);
        emit MoneySumEvent("The money sum is:",moneySum);
        require(moneySum >= TARGET,"Target is not reached");
        require(msg.sender == owner,"this function can only be called by owner");
        bool success;
        bytes memory result;
        //transfer，从当期合约中将所有金额全部转给msg.sender
        //payable(msg.sender).transfer(address(this).balance);
        //send
        //success = payable(msg.sender).send(address(this).balance);
        //require(success,"send failed");
        //call
        fundersToAmount[msg.sender]=0;
        (success,result) = payable(msg.sender).call{value: address(this).balance}("this is call function");
        require(success,"send failed");
        
    }

    function transferOwnership(address _newOwner) public {
        require(msg.sender ==owner,"this function can only be by owner");
        owner = _newOwner;
    }

    function getUsdOfBalance() external view returns(uint256){
        uint256 usdSum= convertEthToUsd(address(this).balance);
        return usdSum;
    }

    //如果未达到目标金额，就将捐赠的钱返还给用户
    function refund()external{
        require(fundersToAmount[msg.sender]!=0,"there is no fund for you");
        require(convertEthToUsd(address(this).balance) <TARGET,"Target is reached");
        bool success;
        fundersToAmount[msg.sender] =0;
        (success,) = payable(msg.sender).call{value:fundersToAmount[msg.sender]}("");
        require(success,"send eth failed");
        //payable(msg.sender).transfer(address(this).balance);
    }
} 