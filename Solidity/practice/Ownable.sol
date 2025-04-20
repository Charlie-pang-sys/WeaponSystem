// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Ownable{
    address public owner;
    constructor(){
        owner = msg.sender;
    }

    modifier OnlyOwner(){
        require(owner ==msg.sender,"Not Owner");
        _;
    }

    function updateOwner(address _newOwner)public OnlyOwner(){
        require(_newOwner !=address(0),"this address not equest 0");
        owner = _newOwner;
    }

    function onlyOwnCallFunction() public OnlyOwner() view returns(address){
        return owner;
    }

    function allCall()public pure returns(string memory){
        return "allCall";
    }
}