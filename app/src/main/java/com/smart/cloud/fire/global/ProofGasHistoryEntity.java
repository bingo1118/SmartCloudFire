package com.smart.cloud.fire.global;

import java.util.List;

/**
 * Created by Rain on 2018/9/3.
 */
public class ProofGasHistoryEntity {

        private String error="";
        private int errorCode;
        private List<ProofGasEntity> history=null;

        public String getError() {
            return error;
        }
        public void setError(String error) {
            this.error = error;
        }
        public int getErrorCode() {
            return errorCode;
        }
        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }
        public List<ProofGasEntity> getHistory() {
            return history;
        }
        public void setHistory(List<ProofGasEntity> history) {
            this.history = history;
        }


    }
