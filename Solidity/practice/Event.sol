// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Event{
    event Log(string message, uint val);
    //一个event中最多只能有用3个indexed的标记
    //如果标记了indexed，在web.js或者ethers.js中可以直接获取链上数据
    event IndexedLog(address indexed sender, uint val);
    function example() external {
        emit Log("foo",1234);
        emit IndexedLog(msg.sender,789);
    }

    event Message(address indexed _from, address indexed _to, string message);
    function sendMessage(address _to,string calldata message)external {
        emit Message(msg.sender, _to,message);
    }
}