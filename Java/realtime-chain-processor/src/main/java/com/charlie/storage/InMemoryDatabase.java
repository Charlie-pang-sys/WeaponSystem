package com.charlie.storage;

import com.charlie.model.NftTransferEventData;
import com.charlie.model.TransactionData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName: com.charlie.storage
 * @Author 彭仁杰
 * @Date 2025/4/26 17:08
 * @Description
 **/
@Component
public class InMemoryDatabase {

    private final List<TransactionData> transactions = new ArrayList<>();
    private final List<NftTransferEventData> nftTransfers = new ArrayList<>();

    public void saveTransaction(TransactionData tx) {
        transactions.add(tx);
    }

    public void saveNftTransfer(NftTransferEventData event) {
        nftTransfers.add(event);
    }

    public List<TransactionData> getTransactions() {
        return transactions;
    }

    public List<NftTransferEventData> getNftTransfers() {
        return nftTransfers;
    }
}

