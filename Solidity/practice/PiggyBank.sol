// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract PiggyBank{
    address public owner = msg.sender;

    event ReceiveLog(address addr,uint amount);
    event WithdrawLog(address addr,uint amount);
    receive() external payable{
        emit ReceiveLog(msg.sender,msg.value);
    }

    function withdraw() external{
        require(msg.sender ==owner,"not owner");
        emit WithdrawLog(msg.sender,address(this).balance);
        selfdestruct(payable(msg.sender));
    }

    function getBalance() external view returns(uint){
        return address(this).balance;
    }
}