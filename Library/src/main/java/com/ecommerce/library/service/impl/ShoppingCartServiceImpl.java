package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private CartItemRepository itemRepository;

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Override
    public ShoppingCart addItemToCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();

        if(cart == null){
            cart = new ShoppingCart();
        }

        double unitPrice = product.getCostPrice();
        int itemQuantity = 0;

        Set<CartItem> cartItems = cart.getCartItem();
        CartItem cartItem = findCartItem(cartItems, product.getId());
        if(cartItems == null){
            cartItems = new HashSet<>();
            if(cartItem == null){
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setTotalPrice(quantity * product.getCostPrice());
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setCart(cart);
                cartItems.add(cartItem);
                itemRepository.save(cartItem);
            }else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
                itemRepository.save(cartItem);
            }
        }else{
           if(cartItem == null){
               cartItem = new CartItem();
               cartItem.setProduct(product);
               cartItem.setTotalPrice(quantity * product.getCostPrice());
               cartItem.setQuantity(quantity);
               cartItem.setUnitPrice(unitPrice);
               cartItem.setCart(cart);
               cartItems.add(cartItem);
               itemRepository.save(cartItem);
           }else{
               itemQuantity = cartItem.getQuantity() + quantity;
               cartItem.setQuantity(itemQuantity);
               itemRepository.save(cartItem);
           }
        }
        cart.setCartItem(cartItems);
        int totalItems = totalItem(cart.getCartItem());
        double totalPrice = totalPrice(cart.getCartItem());

        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);
        cart.setCustomer(customer);

        return cartRepository.save(cart);
    }


    private CartItem findCartItem(Set<CartItem> cartItems,Long productId){
        if(cartItems == null){
            return null;
        }

        CartItem cartItem = null;

        for(CartItem item : cartItems){
            if(item.getProduct().getId() == productId){
                cartItem = item;
            }
        }
        return cartItem;

    }

    private int totalItem(Set<CartItem> cartItemList) {
        int totalItem = 0;
        for (CartItem item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPrice(Set<CartItem> cartItemList) {
        double totalPrice = 0.0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }


    @Override
    public ShoppingCart updateItemInCart(Product product, int quantity, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();

        Set<CartItem> cartItems = cart.getCartItem();

        CartItem item = findCartItem(cartItems,product.getId());

        item.setQuantity(quantity);
        item.setTotalPrice(quantity*product.getCostPrice());
        itemRepository.save(item);

        int totalItems = totalItem(cartItems);
        double totalPrice = totalPrice(cartItems);

        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);
        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart deleteItemFromCart(Product product, Customer customer) {
        ShoppingCart cart = customer.getShoppingCart();
        Set<CartItem> cartItems = cart.getCartItem();
        CartItem item = findCartItem(cartItems, product.getId());

        cartItems.remove(item);
        itemRepository.delete(item);

        double totalPrice = totalPrice(cartItems);
        int totalItems = totalItem(cartItems);

        cart.setCartItem(cartItems);
        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrice);

        return cartRepository.save(cart);

    }
}
