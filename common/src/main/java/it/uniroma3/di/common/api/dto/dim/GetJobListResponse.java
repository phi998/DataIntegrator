package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetJobListResponse {

    private List<GetJobInfoResponse> getJobInfoResponseList;

    public GetJobListResponse() {
        getJobInfoResponseList = new ArrayList<>();
    }

    public void addJobInfoResponse(GetJobInfoResponse getJobInfoResponse) {
        this.getJobInfoResponseList.add(getJobInfoResponse);
    }

}
