// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract FunctionSelector{
    //入参是下面的Receiver合约的transfer方法，入参："transfer(address,uint256)"
    function getSelector(string calldata _func) external pure returns(bytes4){
        //返回了0xa9059cbb
        return bytes4(keccak256(bytes(_func)));
    }
}

contract Receiver{
    event Log(bytes data);
    //接收transfer方法，_to的内容是0x4B20993Bc481177ec7E8f571ceCaE8A9e22C02db，和_amount的入参是10
    function transfer(address _to, uint _amount) external {
        //链上msg.data打印出来的日志是0xa9059cbb0000000000000000000000004b20993bc481177ec7e8f571cecae8a9e22c02db000000000000000000000000000000000000000000000000000000000000000a
        /**
         * 0xa9059cbb是transfer的函数选择器，
         * 0x0000000000000000000000004b20993bc481177ec7e8f571cecae8a9e22c02db是to参数的内容，
         * 0x000000000000000000000000000000000000000000000000000000000000000a是amount参数的内容
         */
        emit Log(msg.data);
    }
}