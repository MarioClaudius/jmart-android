package com.marioJmartDR;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Payment;
import com.marioJmartDR.model.Product;
import com.marioJmartDR.request.RequestFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListInvoiceAdapter extends RecyclerView.Adapter<ListInvoiceAdapter.ListViewHolder> {
    private List<Payment> listPayment;
    private Product product;
    private Account account;
    private static final Gson gson = new Gson();

    public ListInvoiceAdapter(List<Payment> list){
        this.listPayment = list;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invoice_history, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListInvoiceAdapter.ListViewHolder holder, int position) {
        Payment payment = listPayment.get(position);
        Payment.Record lastRecord = payment.history.get(payment.history.size() - 1);
//        account = LoginActivity.getLoggedAccount();

//        Response.Listener<String> listenerAccount = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject object = null;
//                    object = new JSONObject(response);
//                    account = gson.fromJson(response, Account.class);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };

        Response.Listener<String> listenerProduct = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    product = gson.fromJson(response, Product.class);
                    if(product != null){
                        holder.tvProduct.setText(product.name);
                        holder.tvTotalPrice.setText("Rp " + (product.price * payment.productCount));
//                        holder.tvBuyerName.setText(account.store.name);
                        holder.tvDate.setText(lastRecord.date.toString());
                        holder.tvShipment.setText(getShipmentPlan(payment));
                        holder.tvAddress.setText(payment.shipment.address);
                        holder.tvProductCount.setText("" + payment.productCount);
                        holder.tvStatus.setText(lastRecord.status.toString());
                        holder.tvMessage.setText(lastRecord.message);

                        Response.Listener<String> listenerAccount = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = null;
                                    object = new JSONObject(response);
                                    account = gson.fromJson(response, Account.class);
                                    holder.tvBuyerName.setText(account.store.name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        Response.ErrorListener errorListener = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(holder.cvPaymentInvoice.getContext(), "Take Information Failed", Toast.LENGTH_SHORT).show();
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(holder.tvBuyerName.getContext());
                        queue.add(RequestFactory.getById("account", product.accountId, listenerAccount, errorListener));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(holder.cvPaymentInvoice.getContext(), "Take Information Failed", Toast.LENGTH_SHORT).show();
            }
        };

//        holder.tvBuyerName.setText("" + payment.buyerId);
//        holder.tvDate.setText(lastRecord.date.toString());
//        holder.tvShipment.setText(getShipmentPlan(payment));
//        holder.tvAddress.setText(payment.shipment.address);
//        holder.tvProductCount.setText("" + payment.productCount);
//        holder.tvStatus.setText(lastRecord.status.toString());
//        holder.tvMessage.setText(lastRecord.message);
        holder.cvPaymentInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.tvBuyerName.getContext(), "CardView Clicked!!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(holder.tvBuyerName.getContext());
        queue.add(RequestFactory.getById("product", payment.productId, listenerProduct, errorListener));
//        queue.add(RequestFactory.getById("account", product.accountId, listenerAccount, errorListener));
    }

    @Override
    public int getItemCount() {
        return listPayment.size();
    }

    private String getShipmentPlan(Payment payment){
        String shipmentPlan;
        if(payment.shipment.plan == 1){
            shipmentPlan = "INSTANT";
        }
        else if(payment.shipment.plan == 2){
            shipmentPlan = "SAME DAY";
        }
        else if(payment.shipment.plan == 4){
            shipmentPlan = "NEXT DAY";
        }
        else if(payment.shipment.plan == 8){
            shipmentPlan = "REGULER";
        }
        else {
            shipmentPlan = "KARGO";
        }
        return shipmentPlan;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView tvBuyerName, tvDate, tvShipment, tvAddress, tvProduct, tvProductCount, tvTotalPrice, tvStatus, tvMessage;
        CardView cvPaymentInvoice;

        public ListViewHolder(View itemView) {
            super(itemView);
            cvPaymentInvoice = itemView.findViewById(R.id.cv_payment_invoice);
            tvBuyerName = itemView.findViewById(R.id.tv_content_buyer_name_payment);
            tvDate = itemView.findViewById(R.id.tv_content_date_payment);
            tvShipment = itemView.findViewById(R.id.tv_content_shipment_payment);
            tvAddress = itemView.findViewById(R.id.tv_content_address_payment);
            tvProduct = itemView.findViewById(R.id.tv_content_product_payment);
            tvProductCount = itemView.findViewById(R.id.tv_content_product_count_payment);
            tvTotalPrice = itemView.findViewById(R.id.tv_content_total_price_payment);
            tvStatus = itemView.findViewById(R.id.tv_content_status_payment);
            tvMessage = itemView.findViewById(R.id.tv_content_message_payment);
        }
    }
}
