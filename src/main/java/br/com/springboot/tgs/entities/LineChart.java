package br.com.springboot.tgs.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LineChart {

    private String[] labels;
    private int[] data;

    public LineChart(String[] labels, int[] data){
        this.labels = labels;
        this.data = data;
    }

    public String[] getLabels(){
        return labels;
    }

    public void setLabels(String[] labels){
        this.labels = labels;
    }

    public int[] getData(){
        return data;
    }

    public void setData(int[] data){
        this.data = data;
    }
}