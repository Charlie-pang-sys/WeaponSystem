// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SendEther{
    constructor() payable{

    }

    receive () external payable {

    }
    //报错+回滚
    function testTransfer(address payable _to) external {
        _to.transfer(123);
    }

    //返回bool类型
    function testSend(address payable _to) external {
        bool success=_to.send(123);
        require(success,"send fail");
    }
    //返回bool类型和data数据（如果调用的是合约，则返回data）
    function testCall(address payable _to)external {
        (bool success,)=_to.call{value:123}("");
        require(success,"send fail");
    }

    function getBalance() public payable returns(uint256){
        return msg.sender.balance;
    }
}

contract Receive{
    event Log(uint amount, uint gas);
    receive () external payable{
        emit Log(msg.value,gasleft());
    }
}