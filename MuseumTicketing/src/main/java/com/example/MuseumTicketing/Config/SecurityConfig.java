package com.example.MuseumTicketing.Config;

//import com.example.MuseumTicketing.Repo.AdminRepo;
//import com.example.MuseumTicketing.Service.Admin.AdminService;
import com.example.MuseumTicketing.Service.Jwt.UsersService;
import com.example.MuseumTicketing.Model.Role;
import com.example.MuseumTicketing.Service.Jwt.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("ALL")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private final UsersService usersService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/beta/api/2factor/**").permitAll()
                        .requestMatchers("/beta/api/payment/**").permitAll()
                        .requestMatchers("/beta/api/qr/**").permitAll()
                        .requestMatchers("/beta/api/details/**").permitAll()
                        .requestMatchers("/beta/api/auth/**").permitAll()
                        .requestMatchers("/beta/api/stime/getSlot").permitAll()
                        .requestMatchers("/beta/api/booking/**").permitAll()
                        .requestMatchers("/beta/api/calEve/eventCal").permitAll()
                        .requestMatchers("/beta/api/holidays/getDayList").permitAll()
                        .requestMatchers("/beta/api/DataEntry2/getFirstSubComplete").permitAll()
                        .requestMatchers("/beta/api/DataEntry2/getAllByCommonId/{id}").permitAll()
                        .requestMatchers("/beta/api/dataType1/getTalk").permitAll()
                        .requestMatchers("/beta/api/topicData/getTopic").permitAll()
                        .requestMatchers("/beta/api/DataEntry1/getMainComplete").permitAll()
                        .requestMatchers("/beta/api/DataEntry1/getSubDataByCommonId").permitAll()
                        .requestMatchers("/beta/api/DataEntry1/getMainId").permitAll()
                        .requestMatchers("/beta/api/fileType/getFileType").permitAll()
                        .requestMatchers("/beta/api/qrcode/getScanDetails").permitAll()
                        .requestMatchers("/beta/api/DataEntry3/getSecondSub").permitAll()
                        .requestMatchers("/beta/api/DataEntry1/download/{fileName:.+}").permitAll()
                        .requestMatchers("/beta/api/guideApp/**").permitAll()
                        .requestMatchers("/beta/api/guideAppQR/**").permitAll()
                        .requestMatchers("/beta/api/jpgData/**").permitAll()
                        .requestMatchers("/beta/api/tribalData/**").permitAll()
                        .requestMatchers("/beta/api/mediaTypeData/**").permitAll()
                        .requestMatchers("/beta/api/category/addCategory").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/getCategory").permitAll()
                        .requestMatchers("/beta/api/category/updateCategory/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/deleteCategory/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/addType").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/getType").permitAll()
                        .requestMatchers("/beta/api/category/getTypeByCategoryId").permitAll()
                        .requestMatchers("/beta/api/category/updateType/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/deleteType/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/addGst").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/getGST").permitAll()
                        .requestMatchers("/beta/api/category/updateGST/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/deleteGst/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/additionalCharge").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/getAdditionalCharge").permitAll()
                        .requestMatchers("/beta/api/category/updateAdditionalCharge/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/deleteAdditionalCharge/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/addPrice").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/getPrice").permitAll()
                        .requestMatchers("/beta/api/category/getPriceByCategory").permitAll()
                        .requestMatchers("/beta/api/category/updatePriceData/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/updatePrice/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/deletePriceById/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/addPaymentMode").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/paymentMode").permitAll()
                        .requestMatchers("/beta/api/category/updatePaymentMode/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/deletePaymentModeById/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/paymentStatus").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/getPaymentStatus").permitAll()
                        .requestMatchers("/beta/api/category/updatePaymentStatus/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/deleteStatus/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/addDistrict").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/getDistrict").permitAll()
                        .requestMatchers("/beta/api/category/addDiscount").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/getDiscountCount").permitAll()
                        .requestMatchers("/beta/api/category/deleteDiscount").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/category/updateDiscountCount").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/slot/addSlot").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/slot/getAllSlot").permitAll()
                        .requestMatchers("/beta/api/slot/updateSlot").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/slot/updateSlotCapacity").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/slot/deleteSlot/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/slot/bookDate").permitAll()
                        .requestMatchers("/beta/api/slot/updateCapacityByDateAndSlotId").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/userReg").hasAnyAuthority(Role.ADMIN.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/spotData/userRegModify").hasAnyAuthority(Role.ADMIN.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/spotData/getAllRegistrationDetails").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/deletePartialRegistration").hasAnyAuthority(Role.ADMIN.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/spotData/confirmPayment").hasAnyAuthority(Role.ADMIN.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/spotData/getAllUser").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/getUserDetailsByDate").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/getUserDetailsByRangeOfDate").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/totalRevenueByDate").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/visitorsCountByDate").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/visitorsCountByRangeOfDate").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/visitsIncomeAndTotalCountUpToNow").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/spotData/monthlyData").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/addEmployee").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/employees").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/employees-and-scanners").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/scanners").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/allTickets").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/uploadImg/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/downloadImg/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/update/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/delete/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/delete-by-name/{name}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/updateRole/{employeeId}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/addPrice").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/deletePrice/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/admin/updatePrice/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/dashboard/**").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/stime/updateShow/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/stime/addstime").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/holidays/addDayData").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/holidays/updateDate/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/holidays/deleteDate/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/calEve/distinctDate").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/calEve/dateData").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/calEve/capacity/{id}").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/beta/api/scanner/**").hasAnyAuthority(Role.SCANNER.name())
                        .requestMatchers("/beta/api/deleteMain/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/deleteByFirstSub/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/deleteSecond/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/DataEntry2/firstSub").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/DataEntry2/genCommonId").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/bgData/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/topicData/addTopic").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/imgData/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/dataType1/addtalk").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/dataType1/delete/{id}").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/DataEntry1/mainT").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/DataEntry1/upload").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/mediaData/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/fileType/addFiletype").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/qrcode/generate").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/DataEntry3/secondSub").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/DataEntry3/generateSSid").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/updateFirst/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/updateMain/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .requestMatchers("/beta/api/updateSecondSub/**").hasAnyAuthority(Role.ADMIN.name(),Role.EMPLOYEE.name(),Role.SCANNER.name())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );

        return http.build();


    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(usersService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
        throws Exception {
        return config.getAuthenticationManager();
    }

}
