// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * 多签钱包的功能
 */
contract MultiSigWallet{
    event Deposit(address indexed sender, uint amount);
    event Submit(uint indexed txId);
    event Approve(address indexed owner,uint indexed txId);
    event Revoke(address indexed owner, uint indexed txId);
    event Execute(uint indexed exId);

    //保存签名人
    address[] public owners;
    //查看某个地址是否是合法的签名人
    mapping(address =>bool) public isOwner;
    //确认数量
    uint public requiredCount;
    struct Transaction{
        //交易发起的目标地址
        address to;
        //交易发送的主币数量（单位是wei）
        uint value;
        //如果to是合约地址，则可以调用合约的一些函数，data就是在这里使用
        bytes data;
        //交易是否发送成功
        bool executed;
    }
    //记录合约中的交易
    Transaction[] public transactions;
    //mapping(交易Id号=>mapping(签名人地址，该签名人是否批准了该交易 ))
    mapping(uint =>mapping(address=>bool)) public approved;

    constructor(address[] memory _owners, uint _requiredCount){
        require(_owners.length >0,"owners required");
        require(_requiredCount>0 && _requiredCount<=_owners.length,"invalid required number of owners");
        for(uint i;i<_owners.length; i++){
            address owner = _owners[i];
            require(owner!=address(0),"invalid owner");
            require(!isOwner[owner],"owner is not unique");

            isOwner[owner] = true;
            owners.push(owner);
        }
        requiredCount = _requiredCount;
    }

    //接收主币
    receive() external payable{
        emit Deposit(msg.sender,msg.value);
    }
    //msg.sender只能是签名者
    modifier onlyOwner(){
        require(isOwner[msg.sender],"not owner");
        _;
    }


    //如果交易id小于transactions.length，说明交易id无效
    modifier txExists(uint _txId){
        require(_txId < transactions.length,"txId does not exist");
        _;
    }

    //判断当前交易的签名人没有批准过这个txId
    modifier notApproved(uint _txId){
        require(!approved[_txId][msg.sender],"txId already approved");
        _;
    }
    //交易没有执行
    modifier notExecuted(uint _txId){
        require(!transactions[_txId].executed,"tx already executed");
        _;
    }

    function submit(address _to, uint _value, bytes calldata _data)external onlyOwner{
        //这种构建stract是一种方式
        //Transaction memory transaction = Transaction(_to,_value,_data,false);
        //transactions.push(transaction);
        transactions.push(Transaction({
            to:_to,
            value:_value,
            data:_data,
            executed:false
        }));
        emit Submit(transactions.length -1);
    }

    //批准交易
    function approve (uint _txId) external onlyOwner txExists(_txId) notApproved(_txId) notExecuted(_txId){
        approved[_txId][msg.sender] = true;
        emit Approve(msg.sender,_txId);
    }

    //获取所有的签名人数量
    function _getApproveCount(uint _txId) private view returns(uint count){
        for(uint i; i<owners.length;i++){
            if(approved[_txId][owners[i]]){
                count+=1;
            }
        }
    }

    //执行方法
    function execute(uint _txId) external txExists(_txId) notExecuted(_txId){
        require(_getApproveCount(_txId) >=requiredCount,"apprivaks <require");
        Transaction storage transaction = transactions[_txId];
        transaction.executed = true;
        (bool success,)=transaction.to.call{value:transaction.value}(transaction.data);
        require(success,"exec fail");
        emit Execute(_txId);
    }

    //在没有执行之前可以撤销该签名
    function revoke(uint _txId) external onlyOwner txExists(_txId) notExecuted(_txId){
        require(approved[_txId][msg.sender],"tx not approved");
        approved[_txId][msg.sender] = false;
        emit Revoke(msg.sender,_txId);
    }

    function getBalance()external view returns(uint balance){
        return address(this).balance;
    }
}