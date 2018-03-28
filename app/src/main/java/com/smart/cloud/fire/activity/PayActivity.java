package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.os.Bundle;

import com.mob.MobSDK;
import com.mob.paysdk.AliPayAPI;
import com.mob.paysdk.MobPayAPI;
import com.mob.paysdk.OnPayListener;
import com.mob.paysdk.PayOrder;
import com.mob.paysdk.PayResult;
import com.mob.paysdk.PaySDK;

import fire.cloud.smart.com.smartcloudfire.R;

public class PayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);



        PayOrder order = new PayOrder();
        order.setOrderNo("订单号");
        order.setAmount(10);
        order.setSubject("支付标题");
        order.setBody("支付主体");

        AliPayAPI alipay = PaySDK.createMobPayAPI(AliPayAPI.class);

        alipay.pay(order, new OnPayListener<PayOrder>() {
            @Override
            public boolean onWillPay(String ticketId, PayOrder order, MobPayAPI api) {
                // TODO 保存本次支付操作的 ticketId
                // 返回false表示不阻止本次支付
                return false;
            }

            @Override
            public void onPayEnd(PayResult payResult, PayOrder order, MobPayAPI api) {
                // TODO 处理支付的结果，成功或失败可以在payResult中获取
            }
        });



    }
}
