[ Ethereum主网 ]
↓
[ web3j订阅区块 + 日志 ]
↓
[ BlockListener / NftEventListener ]
↓
[ TransactionProcessor / NftEventProcessor ]
↓
[ InMemoryDatabase ]
↓
[ Spring Boot API 提供查询 ]

在该项目中listener是监听器，processor是处理器，
listener是项目逻辑的入口，监听主网的区块和日志，然后调用processor来处理。

