package view;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;
import model.MainModel;
import model.sounddata.SoundData;

import java.net.URL;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class AudioPlayer {
    private static Music theme;

    private static Sound gunshot;
    private static Sound achtung;
    private static Sound dying;

    private static MainModel model;
    private static CyclicBarrier barrier;

    public static void init(MainModel model){
        AudioPlayer.model = model;
        AudioPlayer.barrier = model.barrier;

        TinySound.init();

        URL urlTheme = AudioPlayer.class.getResource("/audio/wolf_theme.wav");
        URL urlGunshot = AudioPlayer.class.getResource("/audio/gunshot.wav");
        URL urlAchtung = AudioPlayer.class.getResource("/audio/achtung.wav");
        URL urlDying = AudioPlayer.class.getResource("/audio/dying.wav");

        theme = TinySound.loadMusic(urlTheme);

        gunshot = TinySound.loadSound(urlGunshot);
        achtung = TinySound.loadSound(urlAchtung);
        dying = TinySound.loadSound(urlDying);
    }

    public static void start(){
        //start theme
        Thread themeThread = new Thread(() -> {
            theme.play(true, 0.75);
        });
        themeThread.start();

        Thread audio = new Thread(() -> {
            while (true){
                playSounds(getData());
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        audio.start();
    }

    private static SoundData getData(){
        SoundData data = model.writingToFirst.get() ? model.soundData2 : model.soundData1;

        return data;
    }

    private static void playSounds(SoundData data){
        if (data.gunshot()){
            new Thread(() -> {
                System.out.println(1);
                gunshot.play();
            }).start();
        }

        if (data.achtung()){
            new Thread(() -> {
                System.out.println(1);
                achtung.play();
            }).start();
        }

        if (data.dying()){
            new Thread(() -> {
                System.out.println(1);
                dying.play();
            }).start();
        }
    }
}
