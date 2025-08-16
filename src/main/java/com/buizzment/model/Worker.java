package com.buizzment.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "workers")
@CompoundIndexes({
        @CompoundIndex(name = "uan_index", def = "{'uan_number': 1}", unique = true),
        @CompoundIndex(name = "tender_index", def = "{'active_tenders': 1}"),
        @CompoundIndex(name = "active_index", def = "{'is_active': 1}")
})
public class Worker {
    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    @Indexed(unique = true)
    @Field("uan_number")
    private String uanNumber;

    @Field("contact_number")
    private String contactNumber;

    @Field("bank_details")
    private BankDetails bankDetails;

    @Field("active_tenders")
    private Set<String> tenderIds = new HashSet<>();

    @Field("org_ids") // Stores as array in MongoDB
    private Set<String> orgIds;

    @Field("tags")
    private Set<String> tags = new HashSet<>();

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("is_active")
    private boolean isActive = true;

    @Field("mobile_no")
    private String mobileNo;

    @Field("Family_Member_Name")
    private String familyMemberName;

    @Field("gender")
    private String Gender;

    @Field("DOB")
    private LocalDate dob;

    @Field("DOJ")
    private LocalDate doj;

    @Data
    @Builder
    public static class BankDetails {
        private String accountNumber;
        private String ifscCode;
        private String bankName;
        private String branch;
    }
}