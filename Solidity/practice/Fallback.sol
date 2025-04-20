// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Fallback {
    event Log(string func, address sender, uint val, bytes data);

    //如果在remix的左下角CALLDATA中有入参调用fallback，或者合约中没有receive，只会调用fallback函数
    fallback () external payable{
        emit Log("fallback",msg.sender, msg.value, msg.data);
    }
    //如果没有有入参调用receive（备用函数）
    receive() external payable { 
        emit Log("receive",msg.sender, msg.value, "");
    }
}