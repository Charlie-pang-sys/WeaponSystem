// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/*
1、通过代理合约部署Test1和Test2
2、通过Proxy合约部署合约时，默认的管理员是Proxy合约
Test1的部署地址0xFaeedD320C10a504c9c98923219bb8CE1EE34274
部署Test1的流程：
1、先部署Helper合约
2、调用Helper合约的getBytecode1方法，并将返回的bytecode获取
3、部署Proxy合约，并将第2步的bytecode填到Proxy合约的deploy方法中，点击deploy
	在console中找到emit的Deploy中Test1的address：0x974e9eC84823D5465d8c5DdAe0bD98f4776CA6E6
4、调用Helper合约的getCallData方法，入参为我们自己（钱包）的合约地址
5、修改Proxy的合约地址为我们的地址：调用execute方法入参：
	（1）、Test1的合约地址。（2）第4步getCallData返回的地址。
	最后就可以把Test1中的msg.sender地址改为我们的钱包地址了。
部署Test2的流程：
1、调用Helper合约的getBytecode2方法，入参为：1，2。并将返回的bytecode获取.
2、将第1步的bytecode填到Proxy合约的deploy方法中，并设置代币数量。点击deploy。
3、在console中找到emit的Deploy中Test2的address：0x95c5cc60449DEDed751b21607b270D63A514E8B5
4、在At Address中加载0x95c5cc60449DEDed751b21607b270D63A514E8B5，就可以看到Test2的方法
*/

contract TestContract1{
    address public owner = msg.sender;

    function setOwner(address _owner) public {
        require(owner ==msg.sender,"not owner");
        owner = _owner;
    }
}

contract TestContract2{
    address public owner = msg.sender;
    uint public value = msg.value;
    uint public x;
    uint public y;

    constructor(uint _x, uint _y)payable{
        x = _x;
        y = _y;
    }
}

contract Proxy{
    event Deploy(address);

    //默认接受代币的方法
    fallback() external payable{}

    //该函数可以接受代币，并返回部署合约的地址
    function deploy(bytes memory _code) external payable returns (address addr){
        assembly{
            //create(v,p,v)
            // v=amount of ETH to send
            // p=pointer in memory to start of code
            // n=size of code
            //callvalue() 是获取要发送的代币；add(_code, 0x20)获取_code的位置，并跳过0x20之后开始算；mload(_code)：计算_code的大小
            addr :=create(callvalue(),add(_code, 0x20),mload(_code))
        }
        require(addr != address(0),"Failed to deploy contract!");
        //在事件中记录该地址
        emit Deploy(addr);
    }

    //将Proxy的msg.sender改为我们自己的地址
    //_target 为Test1/Test2的合约地址，_data是调用getCallData方法生成的地址
    function execute(address _target, bytes memory _data) external payable{
        (bool success,) = _target.call{value:msg.value}(_data);
        require(success,"failed");
    }
}

contract Helper{
    //获取TestContract1的机器码，将来bytecode给Proxy合约就可以部署TestContract1的合约
    function getBytecode1() external pure returns (bytes memory){
        bytes memory bytecode = type(TestContract1).creationCode;
        return bytecode;
    }
    /**
     *abi.encodePacked(bytecode,abi.encode(_x,_y)); 
     *通过对TestContract2的bytecode再次打包，
     *并传入TestContract2的构造参数就可以获取最终的TestContract2bytecode
    */
    function getBytecode2(uint _x,uint _y) external pure returns(bytes memory){
        bytes memory bytecode = type(TestContract2).creationCode;
        return abi.encodePacked(bytecode,abi.encode(_x,_y));
    }

    //打包address为新的地址
    function getCallData(address _owner) external pure returns (bytes memory){
        return abi.encodeWithSignature("setOwner(address)",_owner);
    }
}