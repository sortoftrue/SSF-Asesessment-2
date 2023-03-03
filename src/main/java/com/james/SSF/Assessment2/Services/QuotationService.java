package com.james.SSF.Assessment2.Services;

import java.io.StringReader;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.james.SSF.Assessment2.Models.Quotation;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class QuotationService {
    
    public Quotation getQuotations(List<String> items) throws Exception{
        
        Quotation quotation = new Quotation();
        JsonObject jsonresponse = getJson(items);

        if(jsonresponse.containsKey("error")){
            throw new Exception(jsonresponse.getString("error"));
        }

        JsonArray prices = jsonresponse.getJsonArray("quotations");

        System.out.println(jsonresponse.toString());

        quotation.setQuoteId(jsonresponse.getString("quoteId"));

        for (int i = 0; i < prices.size(); i++) {

            System.out.printf("Getting price %d\n", i);

            JsonObject json_item = prices.getJsonObject(i);
            String name = json_item.getString("item");
            Float unitprice = Float.parseFloat(json_item.getJsonNumber("unitPrice").toString());
            
            System.out.printf("%s,%f\n", name,unitprice);

            quotation.addQuotation(name, unitprice);
        }

        return quotation;
    }

    public JsonObject getJson(List<String> items){

        // List<String> items = new LinkedList<>();
        // items.add("apple");
        // items.add("orange");

        JsonArrayBuilder itemListJson = Json.createArrayBuilder();

        for(String item:items){
            itemListJson.add(item);
        }

        JsonArray json = itemListJson.build();

        RequestEntity<String> req = RequestEntity.post("https://quotation.chuklee.com/quotation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).body(json.toString(),String.class);

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);

        String payload = resp.getBody();

        System.out.println(payload);

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jsonresponse = reader.readObject();

        return jsonresponse;
    }

}
