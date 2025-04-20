// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract VerifySig{
    //_signer签名人地址，_message需要签名的消息，签名的结果_sig，判断函数的签名人与恢复出来的签名地址是否相等
    function verify(address _signer, string memory _message, bytes memory _sig)
        external pure returns(bool){
        bytes32 messageHash = getMessageHash(_message);
        bytes32 ethSignedMessageHash = getEthSignedMessageHash(messageHash);
        return recover(ethSignedMessageHash,_sig) == _signer;
    }

    function getMessageHash(string memory _message) public pure returns(bytes32){
        return keccak256(abi.encodePacked(_message));
    }
    
    //对消息进行hash，签名的时候使用
    function getEthSignedMessageHash(bytes32 _messageHash) public pure returns (bytes32){
        return  keccak256(abi.encodePacked("\x19Ethereum Signed Message:\n32",_messageHash));
    }

    //恢复签名函数
    function recover(bytes32 _ethSignedMessageHash, bytes memory _sig) public pure returns(address){
        (bytes32 r,bytes32 s, uint8 v) = _split(_sig);
        return ecrecover(_ethSignedMessageHash, v, r, s);
    }

    function _split(bytes memory _sig) internal pure returns(bytes32 r,bytes32 s,uint8 v){
        require(_sig.length ==65,"invalid signature length");
        assembly{
            //add表示跳过32个字节，获取32个字节
            r :=mload(add(_sig,32))
            s :=mload(add(_sig,64))
            v :=byte(0, mload(add(_sig,96)))
        }
    }
}