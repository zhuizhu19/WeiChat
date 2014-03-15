<#macro html title="致我最爱的李尤">
    <html class="no-js" lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>${title}</title>
        <#include "head.html"/>
    </head>
    <body class="antialiased hide-extras">
    <div class="marketing off-canvas-wrap">
        <div class="inner-wrap">
            <section role="main" class="scroll-container">
                <#nested/>
            </section>
        </div>
    </div>
    </body>
    <#include "foot.html"/>
    </html>
</#macro>
<#import  "spring.ftl" as spring/>