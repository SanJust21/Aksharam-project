package com.example.MuseumTicketing.spotReg.category;

import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionCharge;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionChargeRepo;
import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.category.CategoryRepo;
import com.example.MuseumTicketing.spotReg.category.gst.GSTData;
import com.example.MuseumTicketing.spotReg.category.gst.GSTRepo;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentMode;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentModeRepo;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatusRepo;
import com.example.MuseumTicketing.spotReg.category.price.PriceData;
import com.example.MuseumTicketing.spotReg.category.price.PriceDataRepo;
import com.example.MuseumTicketing.spotReg.category.type.TypeData;
import com.example.MuseumTicketing.spotReg.category.type.TypeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private TypeRepo typeRepo;
    @Autowired
    private GSTRepo gstRepo;
    @Autowired
    private PriceDataRepo priceDataRepo;
    @Autowired
    private PaymentModeRepo paymentModeRepo;
    @Autowired
    private AdditionChargeRepo additionChargeRepo;
    @Autowired
    private PaymentStatusRepo paymentStatusRepo;


    public ResponseEntity<?> addCategory(CategoryData categoryData) {
        categoryRepo.save(categoryData);
        return new ResponseEntity<>(categoryData, HttpStatus.OK);
    }

    public ResponseEntity<List<CategoryData>> getCategory() {
        List<CategoryData>categoryData=categoryRepo.findAll();
        return new ResponseEntity<>(categoryData,HttpStatus.OK);
    }

    public ResponseEntity<?> updateCategory(Integer id, CategoryData categoryData) {
        Optional<CategoryData>categoryDataOptional=categoryRepo.findById(id);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData1 = categoryDataOptional.get();
            categoryData1.setCategory(categoryData.getCategory());
            categoryRepo.save(categoryData1);
            return new ResponseEntity<>(categoryData1,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteCategoryById(Integer id) {
        Optional<CategoryData>categoryDataOptional=categoryRepo.findById(id);
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData =categoryDataOptional.get();
            String name = categoryData.getCategory();
            categoryRepo.delete(categoryData);
            return new ResponseEntity<>(name+" is deleted ",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> addType(TypeData typeData) {
        TypeData typeData1 = new TypeData();
        Optional<CategoryData>categoryDataOptional=categoryRepo.findById(typeData.getCategoryId());
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            typeData1.setCategoryId(categoryData.getId());
        }
        typeData1.setType(typeData.getType());
        return new ResponseEntity<>(typeRepo.save(typeData),HttpStatus.OK);
    }

    public ResponseEntity<?> getTypeDetails() {
        return new ResponseEntity<>(typeRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<List<TypeData>> getTypeDetailsByCategoryId(Integer id) {
        List<TypeData>typeDataList=typeRepo.findByCategoryId(id);
        return new ResponseEntity<>(typeDataList,HttpStatus.OK);
    }

    public ResponseEntity<?> updateTypeDetails(Integer id, TypeData typeData) {
        Optional<TypeData>typeDataOptional=typeRepo.findById(id);
        if (typeDataOptional.isPresent()){
            TypeData typeData1 =typeDataOptional.get();
            typeData1.setType(typeData.getType());
            typeData1.setCategoryId(typeData.getCategoryId());
            typeRepo.save(typeData1);
            return new ResponseEntity<>(typeData1,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteTypeById(Integer id) {
        Optional<TypeData>typeDataOptional=typeRepo.findById(id);
        if (typeDataOptional.isPresent()){
            TypeData typeData =typeDataOptional.get();
            String type = typeData.getType();
            typeRepo.delete(typeData);
            return new ResponseEntity<>(type+" is deleted",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> addGSTDetails(GSTData gstData) {
        GSTData gstData1 =new GSTData();
        Double gstRate = (gstData.getAmount()/100);
        gstData1.setItem(gstData.getItem());
        gstData1.setAmount(gstRate);
        return new ResponseEntity<>(gstRepo.save(gstData1),HttpStatus.OK);
    }

    public ResponseEntity<List<GSTData>> getAllGST() {
        return new ResponseEntity<>(gstRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> updateGST(Integer id, GSTData gstData) {
        Optional<GSTData>gstDataOptional=gstRepo.findById(id);
        if (gstDataOptional.isPresent()){
            GSTData gstData1 = gstDataOptional.get();
            gstData1.setItem(gstData.getItem());
            gstData1.setAmount(gstData.getAmount());
            gstRepo.save(gstData1);
            return new ResponseEntity<>(gstData1,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteGSTById(Integer id) {
        Optional<GSTData>gstDataOptional=gstRepo.findById(id);
        if (gstDataOptional.isPresent()){
            GSTData gstData=gstDataOptional.get();
            String name = gstData.getItem();
            gstRepo.delete(gstData);
            return new ResponseEntity<>(name+" is deleted ",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> additionalChargeAdd(AdditionCharge additionCharge) {
        return new ResponseEntity<>(additionChargeRepo.save(additionCharge),HttpStatus.OK);
    }

    public ResponseEntity<List<AdditionCharge>> getAdditionalCharge() {
        return new ResponseEntity<>(additionChargeRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> updateAdditionalCharge(Integer id, AdditionCharge additionCharge) {
        Optional<AdditionCharge>additionChargeOptional=additionChargeRepo.findById(id);
        if (additionChargeOptional.isPresent()){
            AdditionCharge additionCharge1 = additionChargeOptional.get();
            additionCharge1.setCharge(additionCharge.getCharge());
            additionCharge1.setAddChargeName(additionCharge.getAddChargeName());
            return new ResponseEntity<>(additionChargeRepo.save(additionCharge1),HttpStatus.OK);
        }
        return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteAdditionalCharge(Integer id) {
        Optional<AdditionCharge>additionChargeOptional=additionChargeRepo.findById(id);
        if (additionChargeOptional.isPresent()){
            AdditionCharge additionCharge = additionChargeOptional.get();
            String name =additionCharge.getAddChargeName();
            additionChargeRepo.delete(additionCharge);
            return new ResponseEntity<>(name+" is removed",HttpStatus.OK);
        }
        return new ResponseEntity<>("Id : "+id+" isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> addPrice(PriceData priceData) {
        PriceData priceData1 = new PriceData();
        Optional<CategoryData>categoryDataOptional=categoryRepo.findById(priceData.getCategoryId());
        if (categoryDataOptional.isPresent()){
            CategoryData categoryData = categoryDataOptional.get();
            priceData1.setCategoryId(categoryData.getId());
        }
        Optional<TypeData>typeDataOptional=typeRepo.findById(priceData.getTypeId());
        if (typeDataOptional.isPresent()){
            TypeData typeData = typeDataOptional.get();
            priceData1.setTypeId(typeData.getId());
        }
        priceData1.setPrice(priceData.getPrice());
        priceDataRepo.save(priceData1);
        return new ResponseEntity<>(priceData1,HttpStatus.OK);
    }

    public ResponseEntity<List<PriceData>> getPriceAll() {
        return new ResponseEntity<>(priceDataRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<List<PriceData>> getPriceBytCategoryId(Integer categoryId) {
        List<PriceData>priceDataList=priceDataRepo.findByCategoryId(categoryId);
        if (!priceDataList.isEmpty()){
            return new ResponseEntity<>(priceDataList,HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updatePriceData(Integer id, PriceData priceData) {
        Optional<PriceData>priceDataOptional=priceDataRepo.findById(id);
        if (priceDataOptional.isPresent()){
            PriceData priceData1 =priceDataOptional.get();
            priceData1.setCategoryId(priceData.getCategoryId());
            priceData1.setTypeId(priceData.getTypeId());
            priceData1.setPrice(priceData.getPrice());
            priceDataRepo.save(priceData1);
            return new ResponseEntity<>(priceData1,HttpStatus.OK);
        }
        return new ResponseEntity<>("Id : "+id+" isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updatePriceOnly(Integer id, Integer typeId,Double price) {
        Optional<PriceData>priceDataOptional=priceDataRepo.findByIdAndTypeId(id,typeId);
        if (priceDataOptional.isPresent()){
            PriceData priceData =priceDataOptional.get();
            priceData.setPrice(price);
            priceDataRepo.save(priceData);
            return new ResponseEntity<>(priceData,HttpStatus.OK);
        }
        return new ResponseEntity<>("Id : "+id+"and typeId : "+typeId+" aren't matching",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deletePriceById(Integer id) {
        Optional<PriceData>priceDataOptional=priceDataRepo.findById(id);
        if (priceDataOptional.isPresent()){
            PriceData priceData =priceDataOptional.get();
            Double priceInfo = priceData.getPrice();
            priceDataRepo.delete(priceData);
            return new ResponseEntity<>(priceInfo+" Rupees is deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("Id :"+id+" isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> addPaymentMode(PaymentMode paymentMode) {
        return new ResponseEntity<>(paymentModeRepo.save(paymentMode),HttpStatus.OK);
    }

    public ResponseEntity<List<PaymentMode>> getAllPaymentMode() {
        return new ResponseEntity<>(paymentModeRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> updatePaymentMode(Integer id, PaymentMode paymentMode) {
        Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(id);
        if (paymentModeOptional.isPresent()){
            PaymentMode paymentMode1 = paymentModeOptional.get();
            paymentMode1.setPaymentType(paymentMode.getPaymentType());
            paymentModeRepo.save(paymentMode1);
            return new ResponseEntity<>(paymentMode1,HttpStatus.OK);
        }
        return new ResponseEntity<>("Id isn't valid",HttpStatus.OK);
    }

    public ResponseEntity<?> deletePaymentMode(Integer id) {
        Optional<PaymentMode>paymentModeOptional = paymentModeRepo.findById(id);
        if (paymentModeOptional.isPresent()){
            PaymentMode paymentMode =paymentModeOptional.get();
            String name = paymentMode.getPaymentType();
            paymentModeRepo.delete(paymentMode);
            return new ResponseEntity<>(name+" payment mode is deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("Id is n't valid",HttpStatus.BAD_REQUEST);
    }



    public ResponseEntity<?> addPaymentStatus(PaymentStatus paymentStatus) {
        return new ResponseEntity<>(paymentStatusRepo.save(paymentStatus),HttpStatus.OK);
    }

    public ResponseEntity<List<PaymentStatus>> getPaymentStatus() {
        return new ResponseEntity<>(paymentStatusRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> updatePaymentStatus(Integer id, PaymentStatus paymentStatus) {
        Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(id);
        if (paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus1=paymentStatusOptional.get();
            paymentStatus1.setStatusName(paymentStatus.getStatusName());
            paymentStatusRepo.save(paymentStatus1);
            return new ResponseEntity<>(paymentStatus1,HttpStatus.OK);
        }
        return new ResponseEntity<>("id : "+id+" isn't valid",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deletePaymentStatus(Integer id) {
        Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(id);
        if (paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus=paymentStatusOptional.get();
            String name = paymentStatus.getStatusName();
            paymentStatusRepo.delete(paymentStatus);
            return new ResponseEntity<>(name+" is deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("id : "+id+" isn't valid",HttpStatus.BAD_REQUEST);
    }
}
