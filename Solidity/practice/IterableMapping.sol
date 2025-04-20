// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract IterableMapping{
    mapping(address=>uint)public balances;
    mapping(address=>bool)public inserted;
    address[] public keys;
    function set(address _key,uint256 _val) public {
        if(inserted[_key]){
            revert("this data is exist!");
        }
        balances[_key] = _val;
        inserted[_key] = true;
        keys.push(_key);
    }

    function getFirstAddBalance() public view returns(uint){
        address firstAdd = keys[0];
        return balances[firstAdd];
    }

    function getLastAddBalance() public view returns(uint){
        address lastAdd = keys[keys.length -1];
        return balances[lastAdd];
    }
}