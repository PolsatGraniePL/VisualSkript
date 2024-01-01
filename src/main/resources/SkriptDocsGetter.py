import requests, html2text

file = requests.get('https://docs.skriptlang.org/docs.html').text
fileWrite = open("SkriptParserYaml.yml", "w", encoding="utf8")

h = html2text.HTML2Text()
h.ignore_links = True


text = file
temp1 = text.split('<div id="content"> ')
temp2 = temp1[1]
temp3 = temp2.split(''' </div> </div><img style="z-index: 99;" src="./Skript Documentation''')
temp4 = temp3[0]
temp5 = temp4.split(' </body> </html> ')
temp6 = temp5[0]
temp7 = temp6.split(' </div> </div> </div> </div>')
temp8 = temp7[:-1]
text = temp8


for i in text:
    id_start = i.find(' id="') + len(' id="')
    id_end = i.find('" data-keywords')
    id = i[id_start:id_end].strip()

    name_start = i.find('<h1 style="display: inline-block">') + len('<h1 style="display: inline-block">')
    name_end = i.find('</h1>')
    name = i[name_start:name_end].strip()

    type_start = i.find('<p class="item-type">') + len('<p class="item-type">')
    type_end = i.find('</p>', type_start)
    type = i[type_start:type_end].strip()

    if type in ("Type", "Event"):
        id=type+id.capitalize()

    patterns_start = i.find('<ul>')
    patterns_end = i.find('</ul>', patterns_start)
    patterns_content = i[patterns_start:patterns_end]

    patterns = []
    while '<li class="skript-code-block">' in patterns_content:
        pattern_start = patterns_content.find('<li class="skript-code-block">') + len('<li class="skript-code-block">')
        pattern_end = patterns_content.find('</li>')
        pattern = patterns_content[pattern_start:pattern_end].strip()
        patterns.append(h.handle(pattern).replace('\\', '\\\\').replace('"', '\''))
        patterns_content = patterns_content[pattern_end + len('</li>') :]

    description_start = i.find('<div class="item-description">') + len('<div class="item-description">')
    description_end = i.find('</div>', description_start)
    description = i[description_start:description_end].strip()
    description = h.handle(description.replace('\\', '\\\\').replace('\'', '`').replace('"', '\''))

    yaml_output = id+":\n"
    yaml_output += f"    name: \"{name}\"\n"
    yaml_output += f"    type: {type}\n"
    yaml_output += f"    pattern:\n"
    for pattern in patterns:
        yaml_output += f"        - \"{pattern[:-2]}\"\n"
    yaml_output += f"    description: \"{description[:-2]}\"\n"

    fileWrite.write("\n"+yaml_output)