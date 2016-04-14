package logics.models.query;

import exception.CustomException;
import logics.models.db.JavaFile;


public class ComputeProportionOfTwoQueryById implements IComputeAttributeContainer  {



    IComputeAttributeContainer q1;
    IComputeAttributeContainer q2;

    public ComputeProportionOfTwoQueryById(IComputeAttributeContainer firstQueryTotal, IComputeAttributeContainer secondQuery) {
        q1 = firstQueryTotal;
        q2 = secondQuery;
    }

    @Override
    public long executeAndGetResult(String path) {

        try {
            JavaFile jf = QueryList.getInstance().getJavaFileByPath(path);
            long a1 = q1.executeAndGetResult(jf.id);
            long a2 = q2.executeAndGetResult(jf.id);
            if(a1==0) return  0 ;
            float resuult = (((float)(a2) / (float)(a1))*100f);
            if(a1<a2){
                throw new CustomException("Wrong percentage >"+resuult+" "+a1 + " "+a2);

            }
            return (long)(resuult);

        } catch (Exception e) {
            throw new CustomException(e);
        }


    }

    @Override
    public long executeAndGetResult(long id) {
        long a1 = q1.executeAndGetResult(id);
        long a2 = q2.executeAndGetResult(id);
        if(a1==0) return  0 ;
        float resuult = (((float)(a2) / (float)(a1))*100f);
        if(a1<a2){
            throw new CustomException("Wrong percentage >"+resuult+" "+a1 + " "+a2);

        }
        return (long)(resuult);
    }


}
