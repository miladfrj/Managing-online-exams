package ir.maktabsharif.finalprojectphase12.entity;

import ir.maktabsharif.finalprojectphase12.entity.enums.ApprovalStatus;
import ir.maktabsharif.finalprojectphase12.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {
    private Role role;
    private String firstname;
    private String lastname;
    private ApprovalStatus approvalStatus;
}
