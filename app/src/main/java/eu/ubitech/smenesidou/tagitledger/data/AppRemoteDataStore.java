package eu.ubitech.smenesidou.tagitledger.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import eu.ubitech.smenesidou.tagitledger.api.ApiService;
import eu.ubitech.smenesidou.tagitledger.model.evt.Product;
import eu.ubitech.smenesidou.tagitledger.model.nxt.IssueAssetResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TaggedDataResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TimeResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TransferAssetResp;
import rx.Observable;

public class AppRemoteDataStore implements AppDataStore {

    @Inject
    @Named("nxt")
    ApiService apiServiceNxt;

    @Inject
    @Named("evt")
    ApiService apiServiceEvt;

    public AppRemoteDataStore() {
        TagItLedgerApplication.getAppComponent().inject(this);
    }


    @Override
    public Observable<TimeResp> getBlockchainStatus(String getBlockchainStatus) {
        return apiServiceNxt.getBlockchainStatus("getBlockchainStatus");
    }

    @Override
    public Observable<TaggedDataResp> uploadTaggedData(String taggedData, String data,
                                                       String name, String description,
                                                       String tags, String channel,
                                                       String secretPhrase, String feeNQT,
                                                       String publicKey, String deadline) {
        return apiServiceNxt.uploadTaggedData(taggedData, data, name, description, tags, channel, secretPhrase, feeNQT, publicKey, deadline);
    }

    @Override
    public Observable<IssueAssetResp> issueAsset(String issueData, String name,
                                                 String description, String secretPhrase,
                                                 String feeNQT, String publicKey, String quantityQNT,
                                                 String deadline, Boolean broadcast) {
        return apiServiceNxt.issueAsset(issueData, name, description, secretPhrase, feeNQT, publicKey, quantityQNT, deadline, broadcast);
    }

    @Override
    public Observable<TransferAssetResp> transferAsset(String tranferAsset, String asset, String secretPhrase,
                                                      String feeNQT, String publicKey, String quantityQNT,
                                                      String deadline, String recipient) {
        return apiServiceNxt.transferAsset(tranferAsset, asset, secretPhrase, feeNQT, publicKey, quantityQNT, deadline, recipient);
    }

    @Override
    public Observable<List<Product>> getProducts() {
        return apiServiceEvt.getProducts();
    }
}
