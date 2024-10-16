package com.example.MuseumTicketing.spotReg.category;

import com.example.MuseumTicketing.Guide.util.ErrorService;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionCharge;
import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.gst.GSTData;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentMode;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.price.PriceData;
import com.example.MuseumTicketing.spotReg.category.type.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/category")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ErrorService errorService;
    @PostMapping(path = "/addCategory")
    public ResponseEntity<?>addCategory(@RequestBody CategoryData categoryData){
        try {
            return categoryService.addCategory(categoryData);
        }catch (Exception e){
          return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getCategory")
    public ResponseEntity<List<CategoryData>>getAllCategory(){
        try {
            return categoryService.getCategory();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/updateCategory/{id}")
    public ResponseEntity<?>updateCategory(@PathVariable Integer id,@RequestBody CategoryData categoryData){
        try {
            return categoryService.updateCategory(id,categoryData);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteCategory/{id}")
    public ResponseEntity<?>deleteCategory(@PathVariable Integer id){
        try {
            return categoryService.deleteCategoryById(id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/addType")
    public ResponseEntity<?>addType(@RequestBody TypeData typeData){
        try {
            return categoryService.addType(typeData);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getType")
    public ResponseEntity<?>getTypeDetails(){
        try {
            return categoryService.getTypeDetails();
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getTypeByCategoryId")
    public ResponseEntity<List<TypeData>>getTypeByCategory(@RequestParam Integer id){
        try {
            return categoryService.getTypeDetailsByCategoryId(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PutMapping(path = "/updateType/{id}")
    public ResponseEntity<?>updateTypeDetails(@PathVariable Integer id,@RequestBody TypeData typeData){
        try {
            return categoryService.updateTypeDetails(id,typeData);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteType/{id}")
    public ResponseEntity<?>deleteTypeDetails(@PathVariable Integer id){
        try {
            return categoryService.deleteTypeById(id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }


    @PostMapping(path = "/addGst")
    public ResponseEntity<?>addGST(@RequestBody GSTData gstData){
        try {
            return categoryService.addGSTDetails(gstData);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getGST")
    public ResponseEntity<List<GSTData>>getAllGST(){
        try {
            return categoryService.getAllGST();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/updateGST/{id}")
    public ResponseEntity<?>updateGST(@PathVariable Integer id,@RequestBody GSTData gstData){
        try {
            return categoryService.updateGST(id,gstData);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteGst/{id}")
    public ResponseEntity<?>deleteGST(@PathVariable Integer id){
        try {
            return categoryService.deleteGSTById(id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/additionalCharge")
    public ResponseEntity<?>additionalCharge(@RequestBody AdditionCharge additionCharge){
        try {
            return categoryService.additionalChargeAdd(additionCharge);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getAdditionalCharge")
    public ResponseEntity<List<AdditionCharge>>getAdditionalCharge(){
        try {
            return categoryService.getAdditionalCharge();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "updateAdditionalCharge/{id}")
    public ResponseEntity<?>updateAdditionalCharge(@PathVariable Integer id, @RequestBody AdditionCharge additionCharge){
        try {
            return categoryService.updateAdditionalCharge(id,additionCharge);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteAdditionalCharge/{id}")
    public ResponseEntity<?>deleteAdditionalCharge(@PathVariable Integer id){
        try {
            return categoryService.deleteAdditionalCharge(id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/addPrice")
    public ResponseEntity<?>addPrice(@RequestBody PriceData priceData){
        try {
           return categoryService.addPrice(priceData);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getPrice")
    public ResponseEntity<List<PriceData>>getAllPrice(){
        try {
            return categoryService.getPriceAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/getPriceByCategory")
    public ResponseEntity<List<PriceData>>getPriceByCategoryId(@RequestParam Integer categoryId){
        try {
            return categoryService.getPriceBytCategoryId(categoryId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
    }

    @PutMapping(path = "/updatePriceData/{id}")
    public ResponseEntity<?>updatePriceData(@PathVariable Integer id,@RequestBody PriceData priceData){
        try {
            return categoryService.updatePriceData(id,priceData);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PutMapping(path = "/updatePrice/{id}")
    public ResponseEntity<?>updatePriceOnly(@PathVariable Integer id,@RequestParam Integer typeId,
                                            @RequestParam Double price){
        try {
            return categoryService.updatePriceOnly(id,typeId,price);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deletePriceById/{id}")
    public ResponseEntity<?>deletePriceById(@PathVariable Integer id){
        try {
           return categoryService.deletePriceById(id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/addPaymentMode")
    public ResponseEntity<?>paymentModeAdd(@RequestBody PaymentMode paymentMode){
        try {
            return categoryService.addPaymentMode(paymentMode);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/paymentMode")
    public ResponseEntity<List<PaymentMode>>getAllPaymentMode(){
        try {
            return categoryService.getAllPaymentMode();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "/updatePaymentMode/{id}")
    public ResponseEntity<?>updatePaymentMode(@PathVariable Integer id,@RequestBody PaymentMode paymentMode){
        try {
            return categoryService.updatePaymentMode(id,paymentMode);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deletePaymentModeById/{id}")
    public ResponseEntity<?>deletePaymentMode(@PathVariable Integer id){
        try {
            return categoryService.deletePaymentMode(id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @PostMapping(path = "/paymentStatus")
    public ResponseEntity<?>PaymentStatus(@RequestBody PaymentStatus paymentStatus){
        try {
            return categoryService.addPaymentStatus(paymentStatus);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @GetMapping(path = "/getPaymentStatus")
    public ResponseEntity<List<PaymentStatus>>getPaymentStatus(){
        try {
            return categoryService.getPaymentStatus();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(path = "updatePaymentStatus/{id}")
    public ResponseEntity<?>updatePaymentStatus(@PathVariable Integer id,@RequestBody PaymentStatus paymentStatus){
        try {
            return categoryService.updatePaymentStatus(id,paymentStatus);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    @DeleteMapping(path = "/deleteStatus/{id}")
    public ResponseEntity<?>deletePaymentStatus(@PathVariable Integer id){
        try {
            return categoryService.deletePaymentStatus(id);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }
}
