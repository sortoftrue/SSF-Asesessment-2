package com.james.SSF.Assessment2.Controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;

import com.james.SSF.Assessment2.Models.Cart;
import com.james.SSF.Assessment2.Models.Order;
import com.james.SSF.Assessment2.Models.Quotation;
import com.james.SSF.Assessment2.Services.QuotationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PurchaseOrderController {

    @Autowired
    private QuotationService quoteSvc;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {

        Cart cart;

        if(session.getAttribute("sessioncart")==null){
            cart = new Cart();
        } else{
            cart = (Cart) session.getAttribute("sessioncart");
        } 
        
        session.setAttribute("sessioncart", cart);
        model.addAttribute("cart", cart);
        model.addAttribute("order", new Order());
        return "view1";
    }

    @GetMapping("/add")
    public String addItem(@Valid Cart cart, BindingResult binding, Model model, HttpSession session) {
        Cart sessioncart = (Cart) session.getAttribute("sessioncart");

        cart.setItemList(sessioncart.getItemList());

        if (binding.hasErrors()){
            model.addAttribute("cart", cart);
            model.addAttribute("order", new Order());
            return "view1";
        }

        String[] validItems = { "apple", "orange", "bread", "cheese", "chicken", "mineral_water","instant_noodles"};

        if (!checkEqual(cart.getCurrentChoice(), validItems)) {
            ObjectError err = new ObjectError("globalError", "We do not stock %s".formatted(cart.getCurrentChoice()));
            binding.addError(err);
            model.addAttribute("cart", cart);
            model.addAttribute("order", new Order());
            return "view1";
        }

        sessioncart.addItem(cart.getCurrentChoice(), cart.getCurrentQty());

        session.setAttribute("sessioncart", sessioncart);
        model.addAttribute("cart", sessioncart);
        model.addAttribute("order", new Order());

        return "view1";
    }

    @GetMapping("/shippingaddress")
    public String getView2(@Valid Order order, BindingResult binding, Model model, HttpSession session) {

        Cart sessioncart = (Cart) session.getAttribute("sessioncart");
        
        if (sessioncart.getItemList().isEmpty()) {
            ObjectError err = new ObjectError("globalError", "Cart is empty!");
            binding.addError(err);
            model.addAttribute("cart", sessioncart);
            model.addAttribute("order", order);
            return "view1";
        }

        //order = new Order();
        //order.setCart(sessioncart);
        //session.setAttribute("sessionOrder", order);
        model.addAttribute("order", new Order());

        return "view2";
    }

    @GetMapping("/checkout")
    public String getView3(@Valid Order order, BindingResult binding, Model model, HttpSession session) throws Exception{
        
        if (binding.hasErrors()){
            return "view2";
        }

        Cart cart = (Cart) session.getAttribute("sessioncart");
        
        List<String> itemlist = new LinkedList<>();
        for(Map.Entry<String,Integer> entry: cart.getItemList().entrySet()){
            itemlist.add(entry.getKey());
        }

        order.setInvoiceId(UUID.randomUUID().toString());

        Quotation quotation;

        try{
           quotation = quoteSvc.getQuotations(itemlist);
        } catch (Exception e){
            ObjectError err = new ObjectError("globalError", e.getMessage());
            binding.addError(err);
            return "view2";
        }

        order.setTotal(calculateCost(quotation,cart));
        session.invalidate();

        return "view3";
    }

    public boolean checkEqual(String word, String[] args) {

        boolean equals = false;

        for (String text : args) {
            if (word.equals(text)) {
                equals = true;
            }
        }

        return equals;
    }

    public Float calculateCost (Quotation quotation, Cart cart){

        Float cost=0f;

        for(Map.Entry<String,Integer> entry: cart.getItemList().entrySet()){
            Float sub= quotation.getQuotation(entry.getKey()) * entry.getValue();
            cost += sub;
        }
        return cost;
    }
}
