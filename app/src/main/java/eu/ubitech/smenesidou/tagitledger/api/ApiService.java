package eu.ubitech.smenesidou.tagitledger.api;

import java.util.List;

import eu.ubitech.smenesidou.tagitledger.model.evt.Product;
import eu.ubitech.smenesidou.tagitledger.model.nxt.IssueAssetResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TaggedDataResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TimeResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TransferAssetResp;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    String HEADER1 = "Content-Type: application/json";
    String HEADER2 = "Accept: application/json";
    String HEADER3 = "Authorization: Place your key";//TODO: place your EVT key

    @GET("/nxt")
    Observable<TimeResp> getBlockchainStatus(@Query("requestType") String getBlockchainStatus);//to delete

    @POST("/nxt")
    Observable<TaggedDataResp> uploadTaggedData(@Query("requestType") String taggedData,
                                                @Query("data") String data,
                                                @Query("name") String name,
                                                @Query("description") String description,
                                                @Query("tags") String tags,
                                                @Query("channel") String channel,
                                                @Query("secretPhrase") String secretPhrase,
                                                @Query("feeNQT") String feeNQT,
                                                @Query("publicKey") String publicKye,
                                                @Query("deadline")String deadline);

    @POST("/nxt")
    Observable<IssueAssetResp> issueAsset(@Query("requestType") String issueData,
                                          @Query("name") String name,
                                          @Query("description") String description,
                                          @Query("secretPhrase") String secretPhrase,
                                          @Query("feeNQT") String feeNQT,
                                          @Query("publicKey") String publicKye,
                                          @Query("quantityQNT") String quantityQNT,
                                          @Query("deadline")String deadline,
                                          @Query("broadcast")Boolean broadcast);

    @POST("/nxt")
    Observable<TransferAssetResp> transferAsset(@Query("requestType") String transferAsset,
                                                @Query("asset") String asset,
                                                @Query("secretPhrase") String secretPhrase,
                                                @Query("feeNQT") String feeNQT,
                                                @Query("publicKey") String publicKye,
                                                @Query("quantityQNT") String quantityQNT,
                                                @Query("deadline")String deadline,
                                                @Query("recipient") String recipient);

    @Headers({HEADER1,HEADER2,HEADER3})
    @GET("/products")
    Observable<List<Product>> getProducts();
}
