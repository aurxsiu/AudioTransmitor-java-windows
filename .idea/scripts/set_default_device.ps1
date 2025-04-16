param([string]$deviceName)
$device = Get-AudioDevice -List | Where-Object { $_.Name -eq $deviceName }
if ($device) {
    Set-AudioDevice -Index $device.Index
} else {
    Write-Output "找不到设备：$deviceName"
}