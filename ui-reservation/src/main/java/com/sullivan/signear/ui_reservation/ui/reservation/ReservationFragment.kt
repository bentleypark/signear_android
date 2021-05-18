package com.sullivan.signear.ui_reservation.ui.reservation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sullivan.sigenear.ui_reservation.R
import com.sullivan.sigenear.ui_reservation.databinding.ReservationFragmentBinding
import com.sullivan.signear.common.base.BaseFragment
import com.sullivan.signear.common.ex.openDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ReservationFragment : BaseFragment<ReservationFragmentBinding>() {

    private val viewModel: ReservationSharedViewModel by activityViewModels()
    private lateinit var centerArray: Array<String>
    private lateinit var centerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReservationFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setupView() {
        binding.apply {
            centerArray = resources.getStringArray(R.array.center_array)
            spCenter.setItems(centerArray.toList())

            rlSignTranslation.isSelected = true
            rlOnlineTranslation.isSelected = !rlSignTranslation.isSelected

            rlSignTranslation.setOnClickListener {
                rlSignTranslation.isSelected = !rlSignTranslation.isSelected
                rlOnlineTranslation.isSelected = !rlSignTranslation.isSelected
                if (it.isSelected) {
                    tvSignTranslation.typeface = Typeface.DEFAULT_BOLD
                    tvOnlineTranslation.typeface = Typeface.DEFAULT
                }
            }

            rlOnlineTranslation.setOnClickListener {
                rlOnlineTranslation.isSelected = !rlOnlineTranslation.isSelected
                rlSignTranslation.isSelected = !rlOnlineTranslation.isSelected
                if (it.isSelected) {
                    tvOnlineTranslation.typeface = Typeface.DEFAULT_BOLD
                    tvSignTranslation.typeface = Typeface.DEFAULT
                }
            }

            btnCalendar.setOnClickListener {
                openDatePickerSpinner()
            }

            btnCalendar.apply {
                val calendar = Calendar.getInstance()

                text =
                    "${calendar.get(Calendar.MONTH) + 1}월 ${calendar.get(Calendar.DAY_OF_MONTH)}일 ${
                        getCurrentDayOfName(
                            calendar
                        )
                    }"
            }

            btnStartTime.setOnClickListener {
                openStartTimePicker()
            }

            btnEndTime.setOnClickListener {
                openEndTimePicker()
            }

            btnCenter.setOnClickListener {
                spCenter.expand()
            }

            btnReservation.setOnClickListener {
                showDialog()
            }

//            reservationConfirmDialogLayout.apply {
//                bottomSheetBehavior = BottomSheetBehavior.from(confirmDialogLayout)
//                hideDialog()
//            }
        }
    }

    private fun openDatePickerSpinner() {
        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            { _, year, monthOfYear, day ->

                val month = monthOfYear + 1
                calendar.set(year, monthOfYear, day)
                binding.btnCalendar.text = "${month}월 ${day}일 ${getCurrentDayOfName(calendar)}"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog.datePicker.apply {
            calendarViewShown = false
            findViewById<View>(
                Resources.getSystem().getIdentifier("year", "id", "android")
            ).isVisible = false
            minDate = System.currentTimeMillis() - 1000
        }

        dialog.show()
    }

    private fun openStartTimePicker() {
        val dialog = TimePickerDialog(
            requireContext(),
            R.style.CustomTimePickerDialog,
            { _, hourOfDay, minute -> getTimeInfo(binding.btnStartTime, hourOfDay, minute) },
            0,
            0,
            false
        )
        dialog.show()
    }

    private fun openEndTimePicker() {
        val dialog = TimePickerDialog(
            requireContext(),
            R.style.CustomTimePickerDialog,
            { _, hourOfDay, minute -> getTimeInfo(binding.btnEndTime, hourOfDay, minute) },
            0,
            0,
            false
        )
        dialog.apply {

        }
        dialog.show()
    }

    private fun getCurrentDayOfName(calendar: Calendar): String {
        val date = calendar.time
        val simpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    private fun getTimeInfo(view: TextView, hour: Int, minute: Int) {
        if (hour <= 12) {
            if (minute <= 9) {
                view.text = "오전 0$hour:0$minute"
            } else {
                view.text = "오전 0$hour:$minute"
            }
        } else {
            if (minute <= 9) {
                view.text = "오후 0${hour - 12}:0$minute"
            } else {
                view.text = "오후 0${hour - 12}:$minute"
            }
        }
    }


    private fun hideDialog() {

    }

    private fun showDialog() {
        openDialog(ReservationConfirmDialogFragment.newInstance(), "")
    }
}