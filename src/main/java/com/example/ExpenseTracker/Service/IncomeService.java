package com.example.ExpenseTracker.Service;

import com.example.ExpenseTracker.Dto.IncomeResquestDto;
import com.example.ExpenseTracker.Entities.Income;
import com.example.ExpenseTracker.Entities.Type.IncomeType;
import com.example.ExpenseTracker.Entities.User;
import com.example.ExpenseTracker.Exception.ResourceNotFoundException;
import com.example.ExpenseTracker.Repository.IncomeRepo;
import com.example.ExpenseTracker.Repository.UserRepo;
import com.example.ExpenseTracker.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepo incomeRepo;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    public void addIncome(IncomeResquestDto incomeResquestDto) {
        String email=jwtUtil.getCurrenUserEmail();
        User loggedUser = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException());

        Income newIncome=new Income();
        newIncome.setIncomeDesc(incomeResquestDto.getIncomeDesc());
        newIncome.setIncomeType(incomeResquestDto.getIncomeType());
        newIncome.setIncomeAmount(incomeResquestDto.getIncomeAmount());
        newIncome.setIncomeDate(incomeResquestDto.getIncomeDate());
        newIncome.setUser(loggedUser);

        incomeRepo.save(newIncome);
    }

//    public List<Income> getIncome() {
//        String email=jwtUtil.getCurrenUserEmail();
//        User loggedUser=userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException());
//
//        return incomeRepo.findByUser(loggedUser);
//    }

    public List<Income> getIncome(String incomeType, String sortBy, String order) {

        String email = jwtUtil.getCurrenUserEmail();
        User loggedUser = userRepo.findByEmail(email)
                .orElseThrow(ResourceNotFoundException::new);

        String sortField = (sortBy == null || sortBy.isBlank())
                ? "incomeDate"
                : sortBy;

        Sort sort = Sort.by(
                "asc".equalsIgnoreCase(order)
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                sortField
        );

        if (incomeType != null && !incomeType.isBlank()) {
            IncomeType type;
            try {
                type = IncomeType.valueOf(incomeType.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid income type");
            }

            return incomeRepo.findByUserAndIncomeType(loggedUser, type, sort);
        }

        return incomeRepo.findByUser(loggedUser, sort);
    }

    public String deleteIncome(Long incomeId) {
        String email=jwtUtil.getCurrenUserEmail();
        User loggedUser=userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException());

        Income income=incomeRepo.findByIncomeIdAndUser(incomeId,loggedUser).orElseThrow(() -> new ResourceNotFoundException());
        incomeRepo.delete(income);
        return "Deleted Successfully";

    }
}
