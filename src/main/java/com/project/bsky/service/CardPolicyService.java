package com.project.bsky.service;

import com.project.bsky.bean.CardPolicyBean;
import com.project.bsky.model.CardPolicy;
import org.json.JSONArray;

public interface CardPolicyService {
	
    CardPolicy getCardPolicyDate();

	JSONArray updatePolicy(CardPolicyBean requestBean);
}
