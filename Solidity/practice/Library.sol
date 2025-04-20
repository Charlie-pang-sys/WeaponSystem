// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * 关于合约库的案例
 */
library Math{
    function max(uint x, uint y)internal pure returns (uint){
        return x>=y? x : y;
    }
}

contract Test{
    function testMax(uint x,uint y ) external pure returns(uint){
        return Math.max(x,y);
    }
}

library ArrayLib{
    function find(uint[] storage arr, uint x) internal view returns (uint){
        for (uint i = 0;i<arr.length;++i){
            if(arr[i]==x )return i;
        }
        revert("not found");
    }
}
contract TestArray{
    uint[] public arr = [2,6,8];
    using ArrayLib for uint[];
    function testFind(uint index) external view returns(uint){
        //return ArrayLib.find(arr,3);
        //另一种写法
        return arr.find(index);
    }
}