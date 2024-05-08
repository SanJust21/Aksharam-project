package com.example.MuseumTicketing.Service.BasePrice;

import com.example.MuseumTicketing.DTO.PriceRequest;
import com.example.MuseumTicketing.Model.Price;
import com.example.MuseumTicketing.Repo.PriceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PriceRequestService {


    @Autowired
    private PriceRepo priceRepo;

    public List<PriceRequest> getAllPrice() {
        List<Price> priceList = priceRepo.findAll();

        // Creating a list to store UserTypeDTO objects
        List<PriceRequest> priceReq = new ArrayList<>();

        // Looping through each UserType and creating a UserTypeDTO
        for (Price userType : priceList) {

            priceReq.add(mapToPriceRequest(userType));

           }

        return priceReq ;
    }
    public PriceRequest addPrice(PriceRequest priceRequest) {
        String type = priceRequest.getType().toLowerCase();
        String category = priceRequest.getCategory().toLowerCase();

        // Check if a price with the same type and category already exists (case-insensitive)
        Optional<Price> existingPrice = priceRepo.findByTypeIgnoreCaseAndCategoryIgnoreCase(type, category);
        if (existingPrice.isPresent()) {
            throw new IllegalArgumentException("Price with type " + type + " and category " + category + " already exists");
        }
        Price price = new Price();
        price.setType(priceRequest.getType());
        price.setPrice(priceRequest.getPrice());
        price.setCategory(priceRequest.getCategory());
        Price savedPrice = priceRepo.save(price);
        return mapToPriceRequestAdd(savedPrice);
    }

    public void deletePriceById(int id) {
        priceRepo.deleteById(id);
    }

    public PriceRequest updatePrice(int id, PriceRequest updatedPriceRequest) {
        Optional<Price> optionalPrice = priceRepo.findById(id);
        if (optionalPrice.isPresent()) {
            Price existingPrice = optionalPrice.get();
            existingPrice.setType(updatedPriceRequest.getType());
            existingPrice.setPrice(updatedPriceRequest.getPrice());
            existingPrice.setCategory(updatedPriceRequest.getCategory());
            Price updatedPrice = priceRepo.save(existingPrice);
            return mapToPriceRequest(updatedPrice);
        } else {

            throw new IllegalArgumentException("Price with ID " + id + " not found");
        }
    }

    public void deletePriceByTypeAndCategory(String type, String category) {
        priceRepo.deleteByTypeAndCategory(type, category);
    }

    public PriceRequest updatePriceByTypeAndCategory(String type, String category, PriceRequest priceRequest) {

        Optional<Price> optionalPrice = priceRepo.findByTypeAndCategory(type, category);
        if (optionalPrice.isPresent()) {
            Price existingPrice = optionalPrice.get();
            existingPrice.setPrice(priceRequest.getPrice());
            Price updatedPrice = priceRepo.save(existingPrice);
            return mapToPriceRequest(updatedPrice);
        } else {
            throw new IllegalArgumentException("Price with type " + type + " and category " + category + " not found");
        }
    }

    private PriceRequest mapToPriceRequest(Price price) {
        PriceRequest transfer = new PriceRequest();
        transfer.setId(price.getId());
        transfer.setPrice(price.getPrice());
        transfer.setType(price.getType());
        transfer.setCategory(price.getCategory());
        return transfer;
    }
    private PriceRequest mapToPriceRequestAdd(Price price) {
        PriceRequest transfer = new PriceRequest();
        transfer.setId(price.getId());
        transfer.setPrice(price.getPrice());
        transfer.setType(price.getType());
        transfer.setCategory(price.getCategory());
        return transfer;
    }

}

