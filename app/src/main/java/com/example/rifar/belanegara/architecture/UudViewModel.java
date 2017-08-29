package com.example.rifar.belanegara.architecture;

import com.example.rifar.belanegara.UudActivity;
import com.example.rifar.belanegara.module.BindableList;

import java.util.Arrays;

/**
 * Created by asus on 8/29/2017.
 */

public class UudViewModel {
    public BindableList<PasalModel> pasals =
            BindableList.create();

    public UudViewModel() {
        initModel();
    }

    private void initModel() {
        pasals.addAll(Arrays.asList(
           new PasalModel("Pasal 1", "Bab I: Bentuk dan Kedaulatan", new AyatModel[] {
                   new AyatModel("Ayat 1", "Negara Indonesia ialah Negara Kesatuan, yang berbentuk Republik."),
                   new AyatModel("Ayat 2", "Kedaulatan berada di tangan rakyat dan dilaksanakan menurut Undang-Undang Dasar."),
                   new AyatModel("Ayat 3", "Negara Indonesia adalah negara hukum.")
           }),
            new PasalModel("Pasal 2", "Bab II: Majelis Permusyawaratan Rakyat", new AyatModel[] {
                    new AyatModel("Ayat 1", "Majelis Permusyawaratan Rakyat terdiri atas anggota Dewan Perwakilan Rakyat " +
                            "dan anggota Dewan Perwakilan Daerah yang dipilih melalui pemilihan umum dan diatur lebih lanjut " +
                            "dengan undangundang"),
                    new AyatModel("Ayat 2", "Majelis Permusyawaratan Rakyat bersidang sedikitnya sekali dalam lima tahun di ibukota negara."),
                    new AyatModel("Ayat 3", "Segala putusan Majelis Permusyawaratan Rakyat ditetapkan dengan suara yang terbanyak.")
            }),
            new PasalModel("Pasal 3", "Bab II: Majelis Permusyawaratan Rakyat", new AyatModel[] {
                    new AyatModel("Ayat 1", "Majelis Permusyawaratan Rakyat berwenang mengubah dan menetapkan UndangUndang Dasar."),
                    new AyatModel("Ayat 2", "Majelis Permusyawaratan Rakyat melantik Presiden dan/atau Wakil Presiden."),
                    new AyatModel("Ayat 3", "Majelis Permusyawaratan Rakyat hanya dap at memberhentikan Presiden dan/atau " +
                            "Wakil Presiden dalam masa jabatannya menurut UndangUndang Dasar. ")
            })
        ));
    }
}
