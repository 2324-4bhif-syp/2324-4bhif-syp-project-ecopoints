package at.htl.ecopoints.io

import com.github.eltonvs.obd.command.ObdCommand
import com.github.eltonvs.obd.command.ObdProtocols
import com.github.eltonvs.obd.command.Switcher
import com.github.eltonvs.obd.command.at.ResetAdapterCommand
import com.github.eltonvs.obd.command.at.SelectProtocolCommand
import com.github.eltonvs.obd.command.at.SetEchoCommand
import com.github.eltonvs.obd.command.at.SetHeadersCommand
import com.github.eltonvs.obd.command.at.SetLineFeedCommand
import com.github.eltonvs.obd.command.at.SetSpacesCommand
import com.github.eltonvs.obd.command.control.AvailablePIDsCommand
import com.github.eltonvs.obd.command.control.DistanceMILOnCommand
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

val obdSetupCommands = listOf<ObdCommand>(
    ResetAdapterCommand(),
    SetEchoCommand(Switcher.OFF),
    SetLineFeedCommand(Switcher.OFF),
    SetSpacesCommand(Switcher.OFF),
    SetHeadersCommand(Switcher.OFF),
    SelectProtocolCommand(ObdProtocols.AUTO),
)

val obdAvailablePIDsCommands = listOf<ObdCommand>(
    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_01_TO_20),
    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_21_TO_40),
    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_41_TO_60),
    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_61_TO_80),
    AvailablePIDsCommand(AvailablePIDsCommand.AvailablePIDsRanges.PIDS_81_TO_A0),
)

val relevantObdCommands = listOf<ObdCommand>(
    LoadCommand(),
    EngineCoolantTemperatureCommand(),
    FuelPressureCommand(),
    IntakeManifoldPressureCommand(),
    RPMCommand(),
    SpeedCommand(),
    TimingAdvanceCommand(),
    MassAirFlowCommand(),
    ThrottlePositionCommand(),
    RuntimeCommand(),
    DistanceMILOnCommand(),
    FuelPressureCommand(),
    FuelRailGaugePressureCommand(),
    EgrErrorCommand(),
    EgrErrorCommand(),
    FuelLevelCommand(),
    BarometricPressureCommand(),
    AbsoluteLoadCommand(),
    RelativeThrottlePositionCommand(),
    AmbientAirTemperatureCommand(),
    TimeSinceMILOnCommand(),
    FuelTypeCommand(),
    OilTemperatureCommand(),
    FuelConsumptionRateCommand()
)

val carDataCommands = listOf<ObdCommand>(
    RPMCommand(),
//        RpmCleanedResCommand(),
    SpeedCommand(),
//        FuelConsumptionRateCommand(),
//        LoadCommand(),
//        AbsoluteLoadCommand(),
//        ThrottlePositionCommand(),
//        RelativeThrottlePositionCommand(),
    EngineCoolantTemperatureCommand(),
    AirIntakeTemperatureCommand(),
    LoadCommand(),
//        OilTemperatureCommand(),
)