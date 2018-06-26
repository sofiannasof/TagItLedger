package eu.ubitech.smenesidou.tagitledger.data;

import java.util.List;

import eu.ubitech.smenesidou.tagitledger.model.evt.Product;
import eu.ubitech.smenesidou.tagitledger.model.evt.ProductsResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.IssueAssetResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TaggedDataResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TimeResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TransferAssetResp;
import rx.Observable;

public interface AppDataStore {
    //nxt
    Observable<TimeResp> getBlockchainStatus(String getBlockchainStatus);

    Observable<TaggedDataResp> uploadTaggedData(String taggedData, String data, String name,
                                                String description, String tags, String channel,
                                                String secretPhrase, String feeNQT, String publicKey,
                                                String deadline);

    Observable<IssueAssetResp> issueAsset(String issueData, String name,
                                          String description, String secretPhrase,
                                          String feeNQT, String publicKey, String quantityQNT,
                                          String deadline, Boolean broadcast);

    Observable<TransferAssetResp> transferAsset(String tranferAsset, String asset, String secretPhrase,
                                               String feeNQT, String publicKey, String quantityQNT,
                                               String deadline, String recipient);

    //evt
    Observable<List<Product>> getProducts();
}