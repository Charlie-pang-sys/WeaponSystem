// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * encode与encodePacked的区别：
 * 1、encode会在每个参数前添加32字节的偏移量，encodePacked不会
 */
contract HashFunc{
    function hash(string memory text, uint number,address add) external pure returns(bytes32){
        return keccak256(abi.encodePacked(text,number,add));
    }

    /**
     * 在返回值中所有不定长的返回值类型都要加上memory，例如string、bytes、uint[]、struct[]等
     */
    function encode(string memory text0,string memory text1)external pure returns(bytes memory){
        return abi.encode(text0,text1);
    }

    function encodePacked(string memory text0,string memory text1)external pure returns(bytes memory){
        return abi.encodePacked(text0,text1);
    }

    /**
     * 会出现hash碰撞的情况，比如：第一个参数："aaa"，第二个参数："bbb"和第一个参数："aa"，第二个参数："abbb"这两组参数的hash值是相同的，
     * 因此在实际开发中尽量使用abi.encode
     */
    function collision(string memory text0,string memory text1) external pure returns (bytes32){
        return keccak256(abi.encodePacked(text0,text1));
    }
}