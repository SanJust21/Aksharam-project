package com.example.MuseumTicketing.spotReg.category;

import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.Service.Jwt.UsersServiceImpl;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionCharge;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionChargeRepo;
import com.example.MuseumTicketing.spotReg.category.category.CategoryData;
import com.example.MuseumTicketing.spotReg.category.category.CategoryRepo;
import com.example.MuseumTicketing.spotReg.category.discount.DiscountCount;
import com.example.MuseumTicketing.spotReg.category.discount.DiscountCountDto;
import com.example.MuseumTicketing.spotReg.category.discount.DiscountCountRepo;
import com.example.MuseumTicketing.spotReg.category.discount.GetDiscountDto;
import com.example.MuseumTicketing.spotReg.category.district.DistrictData;
import com.example.MuseumTicketing.spotReg.category.district.DistrictRepo;
import com.example.MuseumTicketing.spotReg.category.gst.GSTData;
import com.example.MuseumTicketing.spotReg.category.gst.GSTRepo;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentMode;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentModeRepo;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatusRepo;
import com.example.MuseumTicketing.spotReg.category.price.PriceData;
import com.example.MuseumTicketing.spotReg.category.price.PriceDataRepo;
import com.example.MuseumTicketing.spotReg.category.price.PriceDto;
import com.example.MuseumTicketing.spotReg.category.ticketMode.TicketModeData;
import com.example.MuseumTicketing.spotReg.category.ticketMode.TicketModeRepository;
import com.example.MuseumTicketing.spotReg.category.type.TypeData;
import com.example.MuseumTicketing.spotReg.category.type.TypeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private DistrictRepo districtRepo;
    @Autowired
    private DiscountCountRepo discountCountRepo;
    @Autowired
    private TicketModeRepository modeRepository;
    @Autowired
    private AlphaNumeric alphaNumeric;


    public ResponseEntity<?> addCategory(CategoryData categoryData) {
        String name = categoryData.getCategory();
        Optional<CategoryData> categoryDataOptional = categoryRepo.findByCategory(name);
        if (categoryDataOptional.isPresent()){
            return new ResponseEntity<>("Category : "+name+" is already exist",HttpStatus.CONFLICT);
        }else {
            categoryRepo.save(categoryData);
            return new ResponseEntity<>(categoryData, HttpStatus.OK);
        }

    }

    public ResponseEntity<List<CategoryData>> getCategory() {
        List<CategoryData>categoryData=categoryRepo.findAll();
        return new ResponseEntity<>(categoryData,HttpStatus.OK);
    }

    public ResponseEntity<?> updateCategory(Integer id, CategoryData categoryData) {
        String name = categoryData.getCategory();
        Optional<CategoryData> categoryDataOptional1 = categoryRepo.findByCategory(name);
        if (categoryDataOptional1.isPresent()){
            return new ResponseEntity<>("Category : "+name+" is already exist",HttpStatus.CONFLICT);
        }else {
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
        String name = typeData.getType();
        Optional<TypeData> typeDataOptional = typeRepo.findByType(name);
        if (typeDataOptional.isPresent()){
            return new ResponseEntity<>("Type"+name+" is already Exist",HttpStatus.CONFLICT);
        }else {
            Integer catId = typeData.getCategoryId();
            String typeD = typeData.getType();
            Optional<TypeData> typeData1 = typeRepo.findByCategoryIdAndType(catId,typeD);
            if (typeData1.isPresent()){
                return new ResponseEntity<>("Type Name :"+typeD+" is already exist",HttpStatus.CONFLICT);
            }else {
                TypeData typeDataN = new TypeData();
                Optional<CategoryData>categoryDataOptional=categoryRepo.findById(typeData.getCategoryId());
                if (categoryDataOptional.isPresent()){
                    CategoryData categoryData = categoryDataOptional.get();
                    typeDataN.setCategoryId(categoryData.getId());
                }
                typeDataN.setType(typeData.getType());
                return new ResponseEntity<>(typeRepo.save(typeData),HttpStatus.OK);
            }
        }

    }

    public ResponseEntity<?> getTypeDetails() {
        return new ResponseEntity<>(typeRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<List<PriceDto>> getTypeDetailsByCategoryId(Integer id) {
        if (id==null || id <0){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
        List<TypeData>typeDataList=typeRepo.findByCategoryId(id);
        if (typeDataList.isEmpty()){
            log.info("typeId error");
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
        List<PriceDto> dtoList = new ArrayList<>();
        for (TypeData data :typeDataList){
            log.info("inside for loop : ");
            PriceDto dto = new PriceDto();
            Integer categoryId = data.getCategoryId();
            Integer typeId = data.getId();
            dto.setId(data.getId());
            dto.setTypeId(typeId);
            dto.setTypeName(data.getType());
            dto.setType(data.getType());
            dto.setCategoryId(categoryId);
            dto.setCategoryName(categoryRepo.findById(data.getCategoryId()).map(CategoryData::getCategory).orElse(null));
            Double price = priceDataRepo.findByCategoryIdAndTypeId(categoryId,typeId).map(PriceData::getPrice).orElse(0.0);
            dto.setPrice(price);
            dtoList.add(dto);
        }
        return new ResponseEntity<>(dtoList,HttpStatus.OK);
    }

//    public ResponseEntity<List<PriceDto>> getPriceAndTypeByCategoryId(Integer categoryId) {
//        if (categoryId==null||categoryId>0){
//            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
//        }
//        List<TypeData> typeDataList = typeRepo.findByCategoryId(categoryId);
//        if (typeDataList.isEmpty()){
//            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
//        }
//
//    }

    public ResponseEntity<?> updateTypeDetails(Integer id, TypeData typeData) {
        String name = typeData.getType();
        Optional<TypeData> typeDataOptional1 = typeRepo.findByType(name);
        if (typeDataOptional1.isPresent()){
            return new ResponseEntity<>("Type"+name+" is already Exist",HttpStatus.CONFLICT);
        }else {
            Integer catId = typeData.getCategoryId();
            String typeD = typeData.getType();
            Optional<TypeData> typeData1 = typeRepo.findByCategoryIdAndType(catId,typeD);
            if (typeData1.isPresent()){
                return new ResponseEntity<>("Type Name :"+typeD+" is already exist",HttpStatus.CONFLICT);
            }else {
                Optional<TypeData>typeDataOptional=typeRepo.findById(id);
                if (typeDataOptional.isPresent()){
                    TypeData typeDataN =typeDataOptional.get();
                    typeDataN.setType(typeData.getType());
                    typeDataN.setCategoryId(typeData.getCategoryId());
                    typeRepo.save(typeDataN);
                    return new ResponseEntity<>(typeDataN,HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("id isn't valid",HttpStatus.BAD_REQUEST);
                }
            }
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
        String name = gstData.getItem();
        Optional<GSTData>gstDataOptional = gstRepo.findByItem(name);
        if (gstDataOptional.isPresent()){
            return new ResponseEntity<>("Gst : "+name+" is already exist",HttpStatus.CONFLICT);
        }else {
            GSTData gstData1 =new GSTData();
            Double gstRate = (gstData.getAmount()/100);
            gstData1.setItem(gstData.getItem());
            gstData1.setAmount(gstRate);
            return new ResponseEntity<>(gstRepo.save(gstData1),HttpStatus.OK);
        }
    }

    public ResponseEntity<List<GSTData>> getAllGST() {
        return new ResponseEntity<>(gstRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> updateGST(Integer id, GSTData gstData) {
        String name = gstData.getItem();
        Optional<GSTData>gstDataOptional1 = gstRepo.findByItem(name);
        if (gstDataOptional1.isPresent()){
            return new ResponseEntity<>("Gst : "+name+" is already exist",HttpStatus.CONFLICT);
        }else {
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
        String name = additionCharge.getAddChargeName();
        Optional<AdditionCharge>additionChargeOptional=additionChargeRepo.findByAddChargeName(name);
        if (additionChargeOptional.isPresent()){
            return new ResponseEntity<>("Additional Charge : "+name+" is already exist",HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(additionChargeRepo.save(additionCharge),HttpStatus.OK);
        }
    }

    public ResponseEntity<List<AdditionCharge>> getAdditionalCharge() {
        return new ResponseEntity<>(additionChargeRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> updateAdditionalCharge(Integer id, AdditionCharge additionCharge) {
        String name = additionCharge.getAddChargeName();
        Optional<AdditionCharge>additionChargeOptional1=additionChargeRepo.findByAddChargeName(name);
        if (additionChargeOptional1.isPresent()){
            return new ResponseEntity<>("Additional Charge : "+name+" is already exist",HttpStatus.CONFLICT);
        }else {
            Optional<AdditionCharge>additionChargeOptional=additionChargeRepo.findById(id);
            if (additionChargeOptional.isPresent()){
                AdditionCharge additionCharge1 = additionChargeOptional.get();
                additionCharge1.setCharge(additionCharge.getCharge());
                additionCharge1.setAddChargeName(additionCharge.getAddChargeName());
                return new ResponseEntity<>(additionChargeRepo.save(additionCharge1),HttpStatus.OK);
            }
            return new ResponseEntity<>("Id isn't valid",HttpStatus.BAD_REQUEST);
        }

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
        Optional<PriceData> priceDataOptional = priceDataRepo.findByCategoryIdAndTypeId(priceData.getCategoryId(), priceData.getTypeId());
        if (priceDataOptional.isPresent()){
            return new ResponseEntity<>("This details is already present",HttpStatus.CONFLICT);
        }else {
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
    }

    public ResponseEntity<List<PriceDto>> getPriceAll() {
        List<PriceDto> priceDtoList = new ArrayList<>();
        List<PriceData> priceDataList = priceDataRepo.findAll();
        if (!priceDataList.isEmpty()){
            for (PriceData priceData : priceDataList){
                PriceDto priceDto = new PriceDto();
                priceDto.setId(priceData.getId());
                Optional<CategoryData> categoryDataOptional = categoryRepo.findById(priceData.getCategoryId());
                if (categoryDataOptional.isPresent()){
                    priceDto.setCategoryName(categoryDataOptional.get().getCategory());
                    priceDto.setCategoryId(categoryDataOptional.get().getId());
                }
                Optional<TypeData> typeDataOptional = typeRepo.findById(priceData.getTypeId());
                if (typeDataOptional.isPresent()){
                    priceDto.setTypeName(typeDataOptional.get().getType());
                    priceDto.setTypeId(typeDataOptional.get().getId());
                }
                priceDto.setPrice(priceData.getPrice());
                priceDtoList.add(priceDto);
            }
        }
        return new ResponseEntity<>(priceDtoList,HttpStatus.OK);
    }

    public ResponseEntity<List<PriceData>> getPriceBytCategoryId(Integer categoryId) {
        List<PriceData>priceDataList=priceDataRepo.findByCategoryId(categoryId);
        if (!priceDataList.isEmpty()){
            return new ResponseEntity<>(priceDataList,HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updatePriceData(Integer id, PriceData priceData) {
        Optional<PriceData> priceDataOptional1 = priceDataRepo.findByCategoryIdAndTypeId(priceData.getCategoryId(), priceData.getTypeId());
        if (priceDataOptional1.isPresent()){
            return new ResponseEntity<>("This detail is already present",HttpStatus.CONFLICT);
        }else {
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

    public ResponseEntity<?> addDistrict(DistrictData districtData) {
        try {
            return new ResponseEntity<>(districtRepo.save(districtData),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<DistrictData>> getDistrict() {
        return new ResponseEntity<>(districtRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> addDiscountCount(DiscountCount discountCount) {
        try {
            return new ResponseEntity<>(discountCountRepo.save(discountCount),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<GetDiscountDto>> getDiscountCount() {
        List<GetDiscountDto> getDiscountDtoList = new ArrayList<>();
        List<DiscountCount> discountCountList = discountCountRepo.findAll();
        if (!discountCountList.isEmpty()){
            for (DiscountCount discountCount : discountCountList){
                GetDiscountDto getDiscountDto = new GetDiscountDto();
                getDiscountDto.setId(discountCount.getId());
                getDiscountDto.setUserType(discountCount.getUserType());
                getDiscountDto.setDisCount(discountCount.getDisCount());

                Optional<CategoryData> categoryDataOptional = categoryRepo.findById(discountCount.getCategoryId());
                if (categoryDataOptional.isPresent()){
                    CategoryData categoryData = categoryDataOptional.get();
                    getDiscountDto.setCategoryId(discountCount.getCategoryId());
                    getDiscountDto.setCategoryName(categoryData.getCategory());
                }else {
                    getDiscountDto.setCategoryId(discountCount.getCategoryId());
                    getDiscountDto.setCategoryName(null);
                }
                Optional<TypeData> typeDataOptional = typeRepo.findById(discountCount.getTypeId());
                if (typeDataOptional.isPresent()){
                    TypeData typeData = typeDataOptional.get();
                    getDiscountDto.setTypeId(discountCount.getTypeId());
                    getDiscountDto.setTypeName(typeData.getType());
                }else {
                    getDiscountDto.setTypeId(discountCount.getTypeId());
                    getDiscountDto.setTypeName(null);
                }
                getDiscountDtoList.add(getDiscountDto);
            }
        }
        return new ResponseEntity<>(getDiscountDtoList,HttpStatus.OK);
    }

    public ResponseEntity<?> updateDiscountCount(Integer id, DiscountCountDto countDto) {
        Optional<DiscountCount> discountCountOptional = discountCountRepo.findById(id);
        if (discountCountOptional.isPresent()){
            DiscountCount discountCount = discountCountOptional.get();
            discountCount.setDisCount(countDto.getDisCount());
            discountCount.setUserType(countDto.getUserType());
            discountCountRepo.save(discountCount);
            return new ResponseEntity<>(discountCount,HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id isn't present",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteDiscountById(Integer id) {
        Optional<DiscountCount> discountCountOptional = discountCountRepo.findById(id);
        if (discountCountOptional.isPresent()){
            DiscountCount discountCount = discountCountOptional.get();
            String name = discountCount.getUserType();
            discountCountRepo.delete(discountCount);
            return new ResponseEntity<>(name+" is deleted",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id is not valid",HttpStatus.NOT_FOUND);
        }
    }

    public Map<String, Object> addTicketMode(TicketModeData modeData) {
        Map<String,Object> response = new HashMap<>();
        String name = modeData.getName();

        Optional<TicketModeData> ticketModeDataOptional = modeRepository.findByName(name);
        if (ticketModeDataOptional.isPresent()){
            response.put("Error","Duplicate value");
            response.put("name",name);
            return response;
        }
        TicketModeData data=new TicketModeData();
        data.setName(name);
        data.setModeId(alphaNumeric.generateRandomNumber());
        data.setDescription(modeData.getDescription());
        data.setCreatedAt(LocalDateTime.now());
        modeRepository.save(data);
        response.put("data",data);
        return response;
    }

    public Map<String, Object> getAllTicketMode() {
        Map<String,Object>response=new HashMap<>();
        try {
            List<TicketModeData> ticketModeDataList=modeRepository.findAll();
            if (ticketModeDataList.isEmpty()){
                response.put("message","No data found");
                return response;
            }
            response.put("data",ticketModeDataList);
            return response;
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return response;
        }
    }

    public Map<String, Object> updateTicketMode(String modeId, String name) {
        Map<String,Object>response=new HashMap<>();
        try {
            Optional<TicketModeData> ticketModeDataOptional=modeRepository.findByModeId(modeId);
            if (ticketModeDataOptional.isEmpty()){
                response.put("Error","No data found");
                response.put("modeId",modeId);
                return response;
            }
            TicketModeData modeData =ticketModeDataOptional.get();
            modeData.setName(name);
            modeData.setUpdatedAt(LocalDateTime.now());
            modeRepository.save(modeData);
            response.put("data",modeData);
            return response;
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return response;
        }
    }

    public Map<String, Object> deleteTicketMode(String modeId) {
        Map<String,Object>response=new HashMap<>();
        try {
            Optional<TicketModeData> ticketModeDataOptional=modeRepository.findByModeId(modeId);
            if (ticketModeDataOptional.isEmpty()){
                response.put("Error","No data found");
                response.put("modeId",modeId);
                return response;
            }
            TicketModeData modeData =ticketModeDataOptional.get();
            String name = modeData.getName();
            modeRepository.delete(modeData);
            response.put("data",name+" is deleted.");
            return response;
        }catch (Exception e){
            response.put("Error",e.getMessage());
            return response;
        }
    }
}
