package eu.ubitech.smenesidou.tagitledger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evrythng.thng.resource.model.store.Thng;

import java.util.ArrayList;
import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    List<Thng> mItems;

    public CardAdapter() {
        super();
        mItems = new ArrayList<Thng>();
    }

    public void addData(Thng thng) {
        mItems.add(thng);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Thng thng = mItems.get(i);
        viewHolder._product.setText("Product: " + thng.getProduct());

        viewHolder._id.setText("Id: "+ thng.getId());
        viewHolder._properties.setText("Scanned Properties: "+thng.getProperties().toString());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _product;
        public TextView _id;
        public TextView _properties;

        public ViewHolder(View itemView) {
            super(itemView);
            _product = (TextView) itemView.findViewById(R.id.login);
            _id = (TextView) itemView.findViewById(R.id.repos);
            _properties = (TextView) itemView.findViewById(R.id.blog);
        }
    }
}
