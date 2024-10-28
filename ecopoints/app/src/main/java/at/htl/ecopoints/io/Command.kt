package at.htl.ecopoints.io

import android.R
import com.github.eltonvs.obd.command.ATCommand
import com.github.eltonvs.obd.command.AdaptiveTimingMode
import com.github.eltonvs.obd.command.ObdCommand
import com.github.eltonvs.obd.command.ObdProtocols
import com.github.eltonvs.obd.command.Switcher
import com.github.eltonvs.obd.command.at.BufferDumpCommand
import com.github.eltonvs.obd.command.at.ResetAdapterCommand
import com.github.eltonvs.obd.command.at.SelectProtocolCommand
import com.github.eltonvs.obd.command.at.SetAdaptiveTimingCommand
import com.github.eltonvs.obd.command.at.SetEchoCommand
import com.github.eltonvs.obd.command.at.SetHeadersCommand
import com.github.eltonvs.obd.command.at.SetLineFeedCommand
import com.github.eltonvs.obd.command.at.SetSpacesCommand
import com.github.eltonvs.obd.command.at.SetTimeoutCommand
import com.github.eltonvs.obd.command.control.AvailablePIDsCommand
import com.github.eltonvs.obd.command.control.DistanceMILOnCommand
import com.github.eltonvs.obd.command.control.MILOnCommand
import com.github.eltonvs.obd.command.control.TimeSinceMILOnCommand
import com.github.eltonvs.obd.command.control.TimingAdvanceCommand
import com.github.eltonvs.obd.command.egr.EgrErrorCommand
import com.github.eltonvs.obd.command.engine.AbsoluteLoadCommand
import com.github.eltonvs.obd.command.engine.LoadCommand
import com.github.eltonvs.obd.command.engine.MassAirFlowCommand
import com.github.eltonvs.obd.command.engine.RPMCommand
import com.github.eltonvs.obd.command.engine.RelativeThrottlePositionCommand
import com.github.eltonvs.obd.command.engine.RuntimeCommand
import com.github.eltonvs.obd.command.engine.SpeedCommand
import com.github.eltonvs.obd.command.engine.ThrottlePositionCommand
import com.github.eltonvs.obd.command.fuel.FuelConsumptionRateCommand
import com.github.eltonvs.obd.command.fuel.FuelLevelCommand
import com.github.eltonvs.obd.command.fuel.FuelTypeCommand
import com.github.eltonvs.obd.command.pressure.BarometricPressureCommand
import com.github.eltonvs.obd.command.pressure.FuelPressureCommand
import com.github.eltonvs.obd.command.pressure.FuelRailGaugePressureCommand
import com.github.eltonvs.obd.command.pressure.IntakeManifoldPressureCommand
import com.github.eltonvs.obd.command.temperature.AirIntakeTemperatureCommand
import com.github.eltonvs.obd.command.temperature.AmbientAirTemperatureCommand
import com.github.eltonvs.obd.command.temperature.EngineCoolantTemperatureCommand
import com.github.eltonvs.obd.command.temperature.OilTemperatureCommand

class SetAllToDefaultsCommand : ATCommand() {
    override val tag = "SET_ALL_TO_DEFAULTS"
    override val name = "All To Defaults"
    override val pid = "D"
}

class DLCCommand(value: kotlin.Int) : ATCommand() {
    override val tag = "SET_ALL_TO_DEFAULTS"
    override val name = "All To Defaults"
    override val pid = "D${value}"
}

class MemoryCommand(value: kotlin.Int) : ATCommand() {
    override val tag = "SET_ALL_TO_DEFAULTS"
    override val name = "All To Defaults"
    override val pid = "M${value}"
}

class AllowLongMessagesCommand(value: kotlin.Int) : ATCommand() {
    override val tag = "SET_ALL_TO_DEFAULTS"
    override val name = "All To Defaults"
    override val pid = "AL${value}"
}

//ATZ Reset Adapter
//ATE0 Echo on = 1 off = 0
//ATD All To defaults
//ATD0 Display of the DLC on = 1 off =
//ATE0 Echo on = 1 off = 0
//ATH1 Header on = 1 off = 0
//ATSP Select protocol 0 = Auto
//ATE0 Echo on = 1 off = 0
//ATH1 Header on = 1 off = 0
//ATM0 Memory on = 1 off = 0
//ATS0  Spaces on = 1 off = 0
//ATAT1 Adaptive timing 1 = Auto1 2 = Auto2 0=Off
//ATAL Allow Long(>7byte)
//ATST{hh} Timeout = hh*4 ms
val obdSetupCommands = listOf<ObdCommand>(
    ResetAdapterCommand(),
    SetEchoCommand(Switcher.OFF),
    SetAllToDefaultsCommand(),
    DLCCommand(0),


    SetEchoCommand(Switcher.ON),
    SetHeadersCommand(Switcher.ON),
    SelectProtocolCommand(ObdProtocols.AUTO),
    SetEchoCommand(Switcher.ON),
    MemoryCommand(0),
    SetAdaptiveTimingCommand(AdaptiveTimingMode.AUTO_1),
//    AllowLongMessagesCommand(64),
    SetTimeoutCommand(500),

    SetSpacesCommand(Switcher.OFF),
    SetEchoCommand(Switcher.OFF),
    SetLineFeedCommand(Switcher.OFF),
    SetHeadersCommand(Switcher.OFF),
)

val obdAvailablePIDsCommands = listOf<ObdCommand>(
    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_01_TO_20),
    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_21_TO_40),
//    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_41_TO_60),
//    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_61_TO_80),
//    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_81_TO_A0),
    BufferDumpCommand()
)

val relevantObdCommands = listOf<ObdCommand>(
    LoadCommand(),
    EngineCoolantTemperatureCommand(),
    FuelPressureCommand(),
    IntakeManifoldPressureCommand(),
    RPMCommand(),
    SpeedCommand(),
    MassAirFlowCommand(),
    ThrottlePositionCommand(),
    FuelPressureCommand(),
    FuelRailGaugePressureCommand(),
    BarometricPressureCommand(),
    RelativeThrottlePositionCommand(),
    AmbientAirTemperatureCommand(),
    FuelTypeCommand(),
    OilTemperatureCommand(),
    FuelConsumptionRateCommand()
)

val runFrequencyMap = mapOf(
    LoadCommand() to 1,
    EngineCoolantTemperatureCommand() to 5,
    FuelPressureCommand() to 1,
    IntakeManifoldPressureCommand() to 2,
    RPMCommand() to 1,
    SpeedCommand() to 1,
    MassAirFlowCommand() to 1,
    ThrottlePositionCommand() to 2,
    FuelPressureCommand() to 1,
    FuelRailGaugePressureCommand() to 5,
    FuelLevelCommand() to 10,
    BarometricPressureCommand() to 5,
    RelativeThrottlePositionCommand() to 2,
    AmbientAirTemperatureCommand() to 10,
    FuelTypeCommand() to 100,
    OilTemperatureCommand() to 10,
    FuelConsumptionRateCommand() to 1,
)


//val carDataCommands = listOf<ObdCommand>(
//    RPMCommand(),
////        RpmCleanedResCommand(),
//    SpeedCommand(),
////        FuelConsumptionRateCommand(),
////        LoadCommand(),
////        AbsoluteLoadCommand(),
////        ThrottlePositionCommand(),
////        RelativeThrottlePositionCommand(),
//    EngineCoolantTemperatureCommand(),
//    AirIntakeTemperatureCommand(),
//    LoadCommand(),
////        OilTemperatureCommand(),
//)