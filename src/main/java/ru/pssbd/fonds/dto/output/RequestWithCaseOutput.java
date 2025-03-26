package ru.pssbd.fonds.dto.output;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RequestWithCaseOutput {

    FondOutput fond;

    String availabilityOfUsers;

    public RequestWithCaseOutput(FondOutput fond, String availabilityOfUsers) {
        this.fond = fond;
        this.availabilityOfUsers = availabilityOfUsers;
    }
}
