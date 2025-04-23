// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * 自毁合约案例
 */
contract Kill{
    constructor()payable{

    }
    function kill() external {
        selfdestruct(payable(msg.sender));
    }

    function test() external pure returns(uint){
        return 123;
    }
}

contract Helper{

    function getBalance() external view returns(uint){
        return address(this).balance;
    }

    function callKill(Kill _kill) external {
        _kill.kill();
    }
}