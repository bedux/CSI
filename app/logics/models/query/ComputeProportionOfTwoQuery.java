package logics.models.query;

/**
 * Created by bedux on 29/03/16.
 */
public class ComputeProportionOfTwoQuery implements IComputeAttributeContainer {



    IComputeAttributeContainer q1;
    IComputeAttributeContainer q2;

    public ComputeProportionOfTwoQuery(IComputeAttributeContainer firstQueryTotal, IComputeAttributeContainer secondQuery) {
        q1 = firstQueryTotal;
        q2 = secondQuery;
    }

    @Override
    public long executeAndGetResult(String path) {

        long a1 = q1.executeAndGetResult(path);
        long a2 = q2.executeAndGetResult(path);
        if(a1==0) return  0 ;
        float resuult = (((float)(a2) / (float)(a1))*100f);
        if(a1<a2){
          System.out.println(path);

         }

        return (long)(resuult);
    }
}
