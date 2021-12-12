package com.marioJmartDR;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Invoice;
import com.marioJmartDR.model.Payment;
import com.marioJmartDR.model.Product;
import com.marioJmartDR.request.AcceptPaymentRequest;
import com.marioJmartDR.request.CancelPaymentRequest;
import com.marioJmartDR.request.RequestFactory;
import com.marioJmartDR.request.SubmitPaymentRequest;
import com.marioJmartDR.request.TopUpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Adapter Recycler view untuk menampilkan list
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class ListInvoiceAdapter extends RecyclerView.Adapter<ListInvoiceAdapter.ListViewHolder> {
    private List<Payment> listPayment;
    private Product product;
    private Account account;
    private boolean byAccount;
    private static final Gson gson = new Gson();
    private Dialog dialog;

    /**
     * Constructor adapter RecyclerView
     * @param list list payment yang akan ditampilkan
     * @param byAccount boolean yang merepresentasikan list yang ditampilkan adalah invoice terhadap store atau akun
     */
    public ListInvoiceAdapter(List<Payment> list, boolean byAccount){
        this.listPayment = list;
        this.byAccount = byAccount;
    }

    /**
     * Method untuk membuat holder pada setiap elemen di list
     * @param parent viewGroup yang menampung holder
     * @param viewType
     * @return holder
     */
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invoice_history, parent, false);
        return new ListViewHolder(view);
    }

    /**
     * Method untuk mengatur logic setiap holder
     * @param holder holder
     * @param position posisi holder atau posisi elemen pada list
     */
    @Override
    public void onBindViewHolder(ListInvoiceAdapter.ListViewHolder holder, int position) {
        Payment payment = listPayment.get(position);
        Payment.Record lastRecord = payment.history.get(payment.history.size() - 1);
        boolean isSubmitted = (lastRecord.status == Invoice.Status.ON_DELIVERY || lastRecord.status == Invoice.Status.DELIVERED || lastRecord.status == Invoice.Status.FINISHED);
        holder.btnCancel.setVisibility(View.GONE);
        holder.btnAccept.setVisibility(View.GONE);
        holder.btnSubmit.setVisibility(View.GONE);
        holder.tvReceipt.setVisibility(View.GONE);
        holder.tvContentReceipt.setVisibility(View.GONE);
        String receipt = payment.shipment.receipt;
        getInvoiceList(holder, payment, lastRecord, isSubmitted, receipt);      //mengambil list

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {        //event handler untuk tombol accept
            @Override
            public void onClick(View view) {
                Response.Listener<String> listenerAcceptPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);     //response dalam bentuk boolean
                        if(isAccepted){
                            Toast.makeText(holder.cvPaymentInvoice.getContext(), "Payment Accepted", Toast.LENGTH_SHORT).show();
                            getInvoiceList(holder, payment, new Payment.Record(Invoice.Status.ON_PROGRESS, "Payment Accepted"), isSubmitted, receipt);      //menampilkan ulang list invoice terbaru
                        }
                        else {
                            Toast.makeText(holder.cvPaymentInvoice.getContext(), "Accept failed, Payment cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerAcceptPayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.cvPaymentInvoice.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                };

                AcceptPaymentRequest acceptPaymentRequest = new AcceptPaymentRequest(payment.id, listenerAcceptPayment, errorListenerAcceptPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.cvPaymentInvoice.getContext());
                queue.add(acceptPaymentRequest);
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {        //event handler untuk tombol cancel
            @Override
            public void onClick(View view) {
                Response.Listener<String> listenerCancelPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);     //response berbentuk boolean pertanda keberhasilan untuk melakukan cancel payment
                        if(isAccepted){
                            String totalPriceStr = holder.tvTotalPrice.getText().toString().trim().substring(3);
                            double totalPrice = Double.valueOf(totalPriceStr);          //mengambil nilai total price di payment untuk mengembalikan balance pembeli
                            Response.Listener<String> listener = new Response.Listener<String>() {      //listener top up
                                @Override
                                public void onResponse(String response) {
                                    Boolean object = Boolean.valueOf(response);
                                    if(object){
                                        Toast.makeText(holder.cvPaymentInvoice.getContext(), "Balance returned", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(holder.cvPaymentInvoice.getContext(), "Balance can't returned", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };

                            Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(holder.cvPaymentInvoice.getContext(), "Failed Connection", Toast.LENGTH_SHORT).show();
                                }
                            };
                            //menggunakan top up request untuk mengembalikan balance
                            TopUpRequest topUpRequest = new TopUpRequest(totalPrice, payment.buyerId, listener, errorListener);
                            RequestQueue queue = Volley.newRequestQueue(holder.cvPaymentInvoice.getContext());
                            queue.add(topUpRequest);
                            Toast.makeText(holder.cvPaymentInvoice.getContext(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
                            //memperbaharui lagi list payment setelah melakukan cancel
                            getInvoiceList(holder, payment, new Payment.Record(Invoice.Status.CANCELLED, "Payment Cancelled"), isSubmitted, receipt);
                        }
                        else {
                            Toast.makeText(holder.cvPaymentInvoice.getContext(), "This payment can't be cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerCancelPayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.cvPaymentInvoice.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                };

                CancelPaymentRequest cancelPaymentRequest = new CancelPaymentRequest(payment.id, listenerCancelPayment, errorListenerCancelPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.cvPaymentInvoice.getContext());
                queue.add(cancelPaymentRequest);
            }
        });

        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {       //event handler untuk tombol submit
            @Override
            public void onClick(View view) {
                dialog = new Dialog(holder.btnSubmit.getContext());
                dialog.setContentView(R.layout.dialog_input_receipt);
                EditText edtReceipt = dialog.findViewById(R.id.edt_input_receipt);
                Button btnInput = dialog.findViewById(R.id.btn_input_receipt);
                dialog.show();
                btnInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String inputReceipt = edtReceipt.getText().toString().trim();
                        dialog.dismiss();
                        Response.Listener<String> listenerSubmitPayment = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Boolean isSubmitted = Boolean.valueOf(response);        //response berbentuk boolean pertanda sudah berhasil melakukan submit atau tidak
                                if(isSubmitted){
                                    Toast.makeText(holder.cvPaymentInvoice.getContext(), "Payment Submitted", Toast.LENGTH_SHORT).show();
                                    //memperbaharui list setelah melakukan submit
                                    getInvoiceList(holder, payment, new Payment.Record(Invoice.Status.ON_DELIVERY, "Payment Submitted"), true, inputReceipt);
                                }
                                else {
                                    Toast.makeText(holder.cvPaymentInvoice.getContext(), "This payment can't be submitted! " + payment.id, Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                        Response.ErrorListener errorListenerSubmitPayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(holder.cvPaymentInvoice.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                            }
                        };
                        SubmitPaymentRequest submitPaymentRequest = new SubmitPaymentRequest(payment.id, inputReceipt, listenerSubmitPayment, errorListenerSubmitPayment);
                        RequestQueue queue = Volley.newRequestQueue(holder.cvPaymentInvoice.getContext());
                        queue.add(submitPaymentRequest);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPayment.size();          //mengambil banyak elemen payment pada list
    }

    private String getShipmentPlan(Payment payment){        //mengambil nilai string shipment berdasarkan informasi dari payment
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

    /**
     * Method untuk mengambil list elemen Payment untuk ditampilkan pada RecyclerView
     * @param holder holder
     * @param payment object Payment pada list
     * @param lastRecord Record terakhir dari suatu Payment
     * @param isSubmitted boolean yang merepresentasikan kondisi apakah payment sudah disubmit atau belum
     * @param receipt receipt
     */
    public void getInvoiceList(ListInvoiceAdapter.ListViewHolder holder, Payment payment, Payment.Record lastRecord, boolean isSubmitted, String receipt){
        Response.Listener<String> listenerProduct = new Response.Listener<String>() {       //listener
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    product = gson.fromJson(response, Product.class);       //mengambil object Product dari response request
                    if(product != null){        //menampilkan informasi product jika tidak null
                        holder.tvProduct.setText(product.name);
                        holder.tvTotalPrice.setText("Rp " + (product.price * payment.productCount));
                        holder.tvDate.setText(lastRecord.date.toString());
                        holder.tvShipment.setText(getShipmentPlan(payment));
                        holder.tvAddress.setText(payment.shipment.address);
                        holder.tvProductCount.setText("" + payment.productCount);
                        holder.tvStatus.setText(lastRecord.status.toString());
                        holder.tvMessage.setText(lastRecord.message);
                        if(isSubmitted && receipt != null){
                            holder.tvReceipt.setVisibility(View.VISIBLE);
                            holder.tvContentReceipt.setText(receipt);
                            holder.tvContentReceipt.setVisibility(View.VISIBLE);
                        }

                        Response.Listener<String> listenerAccount = new Response.Listener<String>() {       //listener
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = null;
                                    object = new JSONObject(response);
                                    account = gson.fromJson(response, Account.class);       //mengambil informasi akun
                                    if(byAccount){      //invoice by account
                                        holder.tvAccountOrStoreName.setText("Store Name :");
                                        holder.tvBuyerName.setText(account.store.name);
                                        holder.btnCancel.setVisibility(View.GONE);
                                        holder.btnAccept.setVisibility(View.GONE);
                                        if(lastRecord.status == Invoice.Status.WAITING_CONFIRMATION){
                                            holder.btnCancel.setVisibility(View.VISIBLE);
                                            holder.btnAccept.setVisibility(View.GONE);
                                            holder.btnSubmit.setVisibility(View.GONE);
                                        }
                                        else {
                                            holder.btnCancel.setVisibility(View.GONE);
                                            holder.btnAccept.setVisibility(View.GONE);
                                            holder.btnSubmit.setVisibility(View.GONE);
                                        }
                                    }
                                    else {              //invoice by store
                                        holder.tvAccountOrStoreName.setText("Buyer Name :");
                                        holder.tvBuyerName.setText(account.name);
                                        if(lastRecord.status == Invoice.Status.WAITING_CONFIRMATION){
                                            holder.btnCancel.setVisibility(View.VISIBLE);
                                            holder.btnAccept.setVisibility(View.VISIBLE);
                                            holder.btnSubmit.setVisibility(View.GONE);
                                        }
                                        else if(lastRecord.status == Invoice.Status.ON_PROGRESS){
                                            holder.btnCancel.setVisibility(View.GONE);
                                            holder.btnAccept.setVisibility(View.GONE);
                                            holder.btnSubmit.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            holder.btnCancel.setVisibility(View.GONE);
                                            holder.btnAccept.setVisibility(View.GONE);
                                            holder.btnSubmit.setVisibility(View.GONE);
                                        }
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

                        RequestQueue queue = Volley.newRequestQueue(holder.tvBuyerName.getContext());
                        if(byAccount){      //mengambil informasi akun berdasarkan id toko
                            queue.add(RequestFactory.getById("account", product.accountId, listenerAccount, errorListener));
                        }
                        else {              //mengambil informasi akun berdasarkan id pembeli
                            queue.add(RequestFactory.getById("account", payment.buyerId, listenerAccount, errorListener));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(holder.cvPaymentInvoice.getContext(), "Take Information Failed", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(holder.tvBuyerName.getContext());
        queue.add(RequestFactory.getById("product", payment.productId, listenerProduct, errorListener));        //memasukkan request untuk mengambil informasi payment berdasarkan id
    }

    /**
     * Class untuk ListViewHolder
     */
    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView tvBuyerName, tvDate, tvShipment, tvAddress, tvProduct, tvProductCount, tvTotalPrice, tvStatus, tvMessage, tvAccountOrStoreName, tvReceipt, tvContentReceipt;
        CardView cvPaymentInvoice;
        Button btnAccept, btnCancel, btnSubmit;

        /**
         * Constructor ListViewHolder yang melakukan inisialisasi seluruh view dalam holder
         * @param itemView view holder
         */
        public ListViewHolder(View itemView) {
            super(itemView);
            cvPaymentInvoice = itemView.findViewById(R.id.cv_payment_invoice);
            tvAccountOrStoreName = itemView.findViewById(R.id.tv_buyer_or_store_name_payment);
            tvBuyerName = itemView.findViewById(R.id.tv_content_buyer_name_payment);
            tvDate = itemView.findViewById(R.id.tv_content_date_payment);
            tvShipment = itemView.findViewById(R.id.tv_content_shipment_payment);
            tvAddress = itemView.findViewById(R.id.tv_content_address_payment);
            tvProduct = itemView.findViewById(R.id.tv_content_product_payment);
            tvProductCount = itemView.findViewById(R.id.tv_content_product_count_payment);
            tvTotalPrice = itemView.findViewById(R.id.tv_content_total_price_payment);
            tvStatus = itemView.findViewById(R.id.tv_content_status_payment);
            tvMessage = itemView.findViewById(R.id.tv_content_message_payment);
            tvReceipt = itemView.findViewById(R.id.tv_receipt_payment);
            tvContentReceipt = itemView.findViewById(R.id.tv_content_receipt_payment);
            btnAccept = itemView.findViewById(R.id.btn_accept_payment);
            btnCancel = itemView.findViewById(R.id.btn_cancel_payment);
            btnSubmit = itemView.findViewById(R.id.btn_submit_payment);
        }
    }
}
