package eu.ubitech.smenesidou.tagitledger;

import eu.ubitech.smenesidou.tagitledger.base.BasePresenter;
import eu.ubitech.smenesidou.tagitledger.base.BaseView;
import eu.ubitech.smenesidou.tagitledger.mqtt.MqttService;

public class MainContract {
    public interface View extends BaseView<Presenter> {

        void updateNxtData(String data);
    }

    public interface Presenter extends BasePresenter, MqttService {
        void loadBlockchainInfo();

        void postTaggedData(String message);

        void issueAsset(String message);

        void transferAsset(String message);

        void loadProducts();
    }
}
