// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

//safe math
contract SafeMath{
    //或报错 call to SafeMath.testUnderflow errored: Error occurred: revert
    function testUnderflow() public pure returns(uint){
        uint x = 0;
        x--;
        return x;
    }

    //会溢出
    function testUncheckedUnderflow() public pure returns (uint){
        uint x = 0;
        unchecked{
            x--;
        }
        return x;
    }

    /**
     *这句话是自定义异常，可以打印出哪个msg.sender调用报错了--非常有用
     如果把error Unauthorized(address caller);
     这句话定义在contract结构体之外，那么该contracts的其他函数、子类也可以使用该异常
    */
    error Unauthorized(address caller);
    address payable owner = payable(msg.sender);
    function withdraw() public {
        if(msg.sender !=owner){
            //revert("error  error error error error error error error error error error error");
            revert Unauthorized(msg.sender);
        }
        owner.transfer(address(this).balance);

    }
}