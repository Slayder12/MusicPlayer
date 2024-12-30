package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isPlay = false
    private var currentSongIndex = 0

    private var songList = mutableListOf(
        R.raw.afterlife_giving_back_the_pain,
        R.raw.aimer_insane_dream,
        R.raw.evanescence_bring_me_to_life,
        R.raw.the_crystal_method_keep_hope_alive,
        R.raw.the_prodigy_invaders_must_die,
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playSound(songList[currentSongIndex])
        initializeVolumeSeekBar()

    }

    private fun playSound(song: Int) {
        binding.playFAB.setOnClickListener{

            if (!isPlay){
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, song)
                    initializeSeekbar()
                }

                mediaPlayer?.start()
                binding.playFAB.setImageResource(R.drawable.baseline_pause_24)
                isPlay = true
            } else {
                if (mediaPlayer != null) mediaPlayer?.pause()
                binding.playFAB.setImageResource(R.drawable.baseline_play_arrow_24)
                isPlay = false

            }

        }

        binding.nextFAB.setOnClickListener{
            playNextSong()
        }

        binding.previousFAB.setOnClickListener{
            playPreviousSong()
        }

        binding.stopFAB.setOnClickListener{
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null

                isPlayPressed()

            }
        }


        binding.seekBarSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun initializeSeekbar() {
        binding.seekBarSB.max = mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBarSB.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seekBarSB.progress = 0
                }
            }
        },0)
    }

    private fun initializeVolumeSeekBar() {
        binding.seekBarVolumeSB.max = 100
        binding.seekBarVolumeSB.progress = 50

        binding.seekBarVolumeSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val volume = progress / 100.0f
                    mediaPlayer?.setVolume(volume, volume)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun playNextSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentSongIndex = (currentSongIndex + 1) % songList.size
        mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex])
        initializeSeekbar()
        mediaPlayer?.start()
        isNotPlayPressed()
    }

    private fun playPreviousSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentSongIndex = if (currentSongIndex - 1 < 0) songList.size - 1 else currentSongIndex - 1
        mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex])
        initializeSeekbar()
        mediaPlayer?.start()

        isNotPlayPressed()

    }

    private fun isPlayPressed() {
        if (isPlay) {
            binding.playFAB.setImageResource(R.drawable.baseline_play_arrow_24)
            isPlay = false
        }
    }

    private fun isNotPlayPressed() {
        if (!isPlay) {
            binding.playFAB.setImageResource(R.drawable.baseline_pause_24)
            isPlay = true
        }
    }

}