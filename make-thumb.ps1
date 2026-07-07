Add-Type -AssemblyName System.Drawing
$bmp = New-Object System.Drawing.Bitmap 400,300
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.Clear([System.Drawing.Color]::FromArgb(102,126,234))
$font = New-Object System.Drawing.Font('Arial',24)
$g.DrawString('Automation Event',$font,[System.Drawing.Brushes]::White,20,120)
$path = Join-Path (Get-Location) 'src\test\resources\event-thumbnail.png'
$bmp.Save($path,[System.Drawing.Imaging.ImageFormat]::Png)
$g.Dispose()
$bmp.Dispose()
Write-Output ('Saved: ' + $path)
Test-Path $path
