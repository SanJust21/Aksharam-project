package com.example.MuseumTicketing.onlineTicket;

import com.example.MuseumTicketing.onlineTicket.foreginer.ForeignerUserOnline;
import com.example.MuseumTicketing.onlineTicket.foreginer.ForeignerUserOnlineRepository;
import com.example.MuseumTicketing.onlineTicket.institutionUser.InstitutionUserOnline;
import com.example.MuseumTicketing.onlineTicket.institutionUser.InstitutionUserOnlineRepository;
import com.example.MuseumTicketing.onlineTicket.publicUser.PublicUserOnline;
import com.example.MuseumTicketing.onlineTicket.publicUser.PublicUserOnlineRepository;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingDetails;
import com.example.MuseumTicketing.spotReg.bookingDetails.booking.BookingSpotRepo;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatusRepo;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionData;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionDataRepo;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerData;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerDataRepo;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicData;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HoldExpiryScheduler {
    @Autowired
    private PublicUserOnlineRepository pUserOnlineRepository;
    @Autowired
    private InstitutionUserOnlineRepository iUserOnlineRepository;
    @Autowired
    private ForeignerUserOnlineRepository fUserOnlineRepository;
    @Autowired
    private PublicRepo publicRepo;
    @Autowired
    private InstitutionDataRepo institutionDataRepo;
    @Autowired
    private ForeignerDataRepo foreignerDataRepo;
    @Autowired
    private PaymentStatusRepo paymentStatusRepo;
    @Autowired
    private BookingSpotRepo bookingSpotRepo;
    @Value("${onlineTicket.lockTime.interval.minutes}")
    private Integer onlineTimeInterval;
    @Value("${spotTicket.lockTime.interval.minutes}")
    private Integer spotTimeInterval;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredHolds(){
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalDateTime currentDateTime = LocalDateTime.now();

        List<PublicData> publicDataList = publicRepo.findByVisitDate(currentDate);
        List<PublicUserOnline> publicUserOnlineList = pUserOnlineRepository.findByBookDate(currentDate);
        if (!publicDataList.isEmpty()){
            for (PublicData pData:publicDataList){
                LocalTime createdAt = pData.getCreatedTime();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                if (currentTime.isAfter(expiryTime) && (pData.getPaymentId() == null || pData.getPaymentId().isBlank()||"Pending".equalsIgnoreCase(getPaymentStatus(pData.getPaymentStatusId())))){
                    restoreCapacity(pData.getVisitDate(),pData.getSlotId(),pData.getCountOfPeople());
                    publicRepo.delete(pData);
                }
            }
        }
        if (!publicUserOnlineList.isEmpty()){
            for (PublicUserOnline userOnline:publicUserOnlineList){
                LocalDateTime createdAt = userOnline.getCreatedAt();
                LocalDateTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                if (currentDateTime.isAfter(expiryTime) && (userOnline.getPaymentId() ==null || userOnline.getPaymentId().isBlank() ||Boolean.FALSE.equals(userOnline.isPaymentStatus()))){
                    restoreCapacity(userOnline.getVisitDate(),userOnline.getSlotId(),userOnline.getCountOfPeople());
                    pUserOnlineRepository.delete(userOnline);
                }
            }
        }

        List<InstitutionData> institutionDataList = institutionDataRepo.findByVisitDate(currentDate);
        List<InstitutionUserOnline> institutionUserOnlineList = iUserOnlineRepository.findByBookDate(currentDate);
        if (!institutionDataList.isEmpty()){
            for (InstitutionData iData:institutionDataList){
                LocalTime createdAt = iData.getCreatedTime();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                String paymentId = iData.getPaymentId();
                if (currentTime.isAfter(expiryTime) && (paymentId==null ||paymentId.isBlank()|| "Pending".equalsIgnoreCase(getPaymentStatus(iData.getPaymentStatusId())))){
                    restoreCapacity(iData.getVisitDate(),iData.getSlotId(),iData.getCountOfPeople());
                    institutionDataRepo.delete(iData);
                }
            }
        }

        if (!institutionUserOnlineList.isEmpty()){
            for (InstitutionUserOnline userOnline:institutionUserOnlineList){
                LocalDateTime createdAt = userOnline.getCreatedAt();
                LocalDateTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                String paymentId = userOnline.getPaymentId();
                Boolean paymentStatus = userOnline.isPaymentStatus();
                if (currentDateTime.isAfter(expiryTime)&&(paymentId==null||paymentId.isBlank()||Boolean.FALSE.equals(paymentStatus))){
                    restoreCapacity(userOnline.getVisitDate(),userOnline.getSlotId(),userOnline.getCountOfPeople());
                    iUserOnlineRepository.delete(userOnline);
                }
            }
        }

        List<ForeignerData> foreignerDataList = foreignerDataRepo.findByVisitDate(currentDate);
        List<ForeignerUserOnline> foreignerUserOnlineList = fUserOnlineRepository.findByBookDate(currentDate);
        if (!foreignerDataList.isEmpty()){
            for (ForeignerData fData:foreignerDataList){
                LocalTime createdAt = fData.getCreatedTime();
                LocalTime expiryTime = createdAt.plusMinutes(spotTimeInterval);
                String paymentId = fData.getPaymentId();
                if (currentTime.isAfter(expiryTime)&&(paymentId==null ||paymentId.isBlank()||"Pending".equalsIgnoreCase(getPaymentStatus(fData.getPaymentStatusId())))){
                    restoreCapacity(fData.getVisitDate(),fData.getSlotId(),fData.getCountOfPeople());
                    foreignerDataRepo.delete(fData);
                }
            }
        }
        if (!foreignerUserOnlineList.isEmpty()){
            for (ForeignerUserOnline userOnline:foreignerUserOnlineList){
                LocalDateTime createdAt = userOnline.getCreatedAt();
                LocalDateTime expiryTime = createdAt.plusMinutes(onlineTimeInterval);
                String paymentId = userOnline.getPaymentId();
                if (currentDateTime.isAfter(expiryTime)&&(paymentId==null||paymentId.isBlank()||Boolean.FALSE.equals(userOnline.isPaymentStatus()))){
                    restoreCapacity(userOnline.getVisitDate(),userOnline.getSlotId(),userOnline.getCountOfPeople());
                    fUserOnlineRepository.delete(userOnline);
                }
            }
        }
    }

    @Transactional
    private void restoreCapacity(LocalDate visitDate, Integer slotId, Integer countOfPeople) {
        Optional<BookingDetails> bookingDetailsOptional = bookingSpotRepo.findByBookDateAndSlotIdForUpdate(visitDate,slotId);
        if (bookingDetailsOptional.isPresent()){
            BookingDetails bDetails = bookingDetailsOptional.get();
            bDetails.setPresentCapacity(bDetails.getPresentCapacity()+countOfPeople);
            bookingSpotRepo.save(bDetails);
        }
    }

    private String getPaymentStatus(Integer paymentStatusId) {
        String paymentStatus = paymentStatusRepo.findById(paymentStatusId).map(PaymentStatus::getStatusName).orElse(null);
        return paymentStatus;
    }
}

