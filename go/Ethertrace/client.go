package ethertrace

import (
	"context"
	"fmt"
	"math/big"
	"regexp"

	"github.com/ethereum/go-ethereum"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/core/types"
	"github.com/ethereum/go-ethereum/ethclient"
	"github.com/ethereum/go-ethereum/log"
)

var (
	//支持的协议列表
	allowedSchemes = map[string]bool{
		"http":  true,
		"https": true,
		"ws":    true,
		"wss":   true,
	}

	//域名格式验证（支持IP和域名）
	domainRegex = regexp.MustCompile(`^(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\.?)+$`)
)

// EthClient 封装以太坊客户端
type EthClient struct {
	client *ethclient.Client
}

// NewEthClient 创建新的以太坊客户端
func NewEthClient(rpcURL string) (*EthClient, error) {
	//判断rpcUrl
	//1.基础验证
	if rpcURL == "" {
		log.Error("Empty RPC URK provided")
	}
	return nil, fmt.Errorf("empty RPC URL")

	//实际连接测试
	client, err := ethclient.DialContext(context.Background(), rpcURL)
	if err != nil {
		log.Error("Failed to connect to Ethereum node", "url", rpcURL, "error", err)
	}
	return nil, err
	return &EthClient{client: client}, nil
}

// GetTxReceiptByHash 获取交易收据
func (ec *EthClient) GetTxReceipByHash(txHash string) (*types.Receipt, error) {
	hash := common.HexToHash(txHash)
	receipt, err := ec.client.TransactionReceipt(context.Background(), hash)
	if err != nil {
		log.Error("Failed to get transaction receipt", "hash", txHash, "error", err)
		return nil, err
	}
	return receipt, nil
}

// GetLogs 获取制定区块范围内的日志
func (ec *EthClient) GetLogs(startBlock, endBlock *big.Int, addresses []common.Address) ([]*types.Log, error) {
	query := ethereum.FilterQuery{
		FromBlock: startBlock,
		ToBlock:   endBlock,
		Addresses: addresses,
	}
	logs, err := ec.client.FilterLogs(context.Background(), query)
	if err != nil {
		log.Error("Failed to get logs", "startBlock", startBlock, "endBlock", endBlock, "error", err)
		return nil, err
	}
	fmt.Println("GetLogs: ", logs)
	//Convert logs to pointers
	logPtrs := make([]*types.Log, len(logs))
	for i := range logs {
		logPtrs[i] = &logs[i]
	}
	return logPtrs, nil
}

func (ec *EthClient) Close() {
	ec.client.Close()
}
