package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Product;
import com.marioJmartDR.model.ProductCategory;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvProductName, tvProductWeight, tvProductPrice, tvProductDiscount, tvProductCondition, tvProductCategory, tvProductShipmentPlan, tvAccountBalance;
    private Button btnPay, btnCancel;
    private Account account;
    private Product productSelected;
    String conditionUsed;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvProductName = findViewById(R.id.tv_product_name_payment);
        tvProductWeight = findViewById(R.id.tv_product_weight_payment);
        tvProductCondition = findViewById(R.id.tv_product_category_payment);
        tvProductCategory = findViewById(R.id.tv_product_category_payment);
        tvProductPrice = findViewById(R.id.tv_product_price_payment);
        tvProductDiscount = findViewById(R.id.tv_product_discount_payment);
        tvProductShipmentPlan = findViewById(R.id.tv_product_shipmentplan_payment);
        tvAccountBalance = findViewById(R.id.tv_account_balance_payment);
        btnPay = findViewById(R.id.btn_pay_payment);
        btnCancel = findViewById(R.id.btn_cancel_payment);
        account = LoginActivity.getLoggedAccount();
        productSelected = MainActivity.getProductSelectedToBuy();
        tvProductName.setText(productSelected.name);
        tvProductWeight.setText(productSelected.weight + " Kg");
        if(productSelected.conditionUsed){
            conditionUsed = "NEW";
        }
        else {
            conditionUsed = "USED";
        }
        tvProductCondition.setText(conditionUsed);
        tvProductCategory.setText(getProductCategory(productSelected.category));
        tvProductPrice.setText("Rp " + productSelected.price);
        tvProductDiscount.setText(productSelected.discount + "%");
        tvProductShipmentPlan.setText(getShipmentPlan(productSelected.shipmentPlans));
        tvAccountBalance.setText("Rp " + account.balance);
        dialog = new Dialog(PaymentActivity.this);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(account.balance >= productSelected.price){
                    dialog.setContentView(R.layout.dialog_confirmation);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnYes = dialog.findViewById(R.id.btn_yes_payment_confirmation);
                    Button btnNo = dialog.findViewById(R.id.btn_no_payment_confirmation);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {
                    dialog.setContentView(R.layout.dialog_failed_payment);
                    MaterialButton btnOk = dialog.findViewById(R.id.btn_ok_failed);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getProductCategory(ProductCategory category){
        String categoryStr = "-";                                //inisialisasi asal
        for(ProductCategory pc : ProductCategory.values()){
            if(pc == category){
                categoryStr = pc.toString();
            }
        }
        return categoryStr;
    }

    public String getShipmentPlan(byte plan){
        String shipmentPlan;
        if(plan == 1){
            shipmentPlan = "INSTANT";
        }
        else if(plan == 2){
            shipmentPlan = "SAME DAY";
        }
        else if(plan == 4){
            shipmentPlan = "NEXT DAY";
        }
        else if(plan == 8){
            shipmentPlan = "REGULER";
        }
        else {
            shipmentPlan = "KARGO";
        }
        return shipmentPlan;
    }
}