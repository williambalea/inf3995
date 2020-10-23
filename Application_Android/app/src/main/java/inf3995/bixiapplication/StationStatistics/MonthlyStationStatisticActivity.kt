package inf3995.bixiapplication.StationStatistics

import android.app.Notification
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Log
import android.view.PointerIcon.load
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import inf3995.bixiapplication.*
import inf3995.test.bixiapplication.R
import kotlinx.android.synthetic.main.activity_coordinates_station.*
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_code
import kotlinx.android.synthetic.main.activity_coordinates_station.Station_name
import kotlinx.android.synthetic.main.activity_monthly_station_statistic.*
import kotlinx.android.synthetic.main.station_list_activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MonthlyStationStatisticActivity : AppCompatActivity() {

    var station : Station? = null
    lateinit var temps: String
    var code: Int = 0
    var annee= 0
    var myImage:ImageView? = null
    var myImageString:String = "iVBORw0KGgoAAAANSUhEUgAAAoAAAAHgCAYAAAA10dzkAAAAOXRFWHRTb2Z0d2FyZQBNYXRwbG90bGliIHZlcnNpb24zLjMuMiwgaHR0cHM6Ly9tYXRwbG90bGliLm9yZy8vihELAAAACXBIWXMAAA9hAAAPYQGoP6dpAABVZElEQVR4nO3dd3gU5f7+8XvTQ0ICCZAECCEUqdJbQKpBEEREECkeQ9NzFFFAPQIqTQRBmkgRFYINVBRRUUDggKLSFWwYioQiJBQhQCCF5Pn9wS/7ZUnoSXaTeb+ua68rO/PMM5+d7GbvzDwzYzPGGAEAAMAy3JxdAAAAAPIXARAAAMBiCIAAAAAWQwAEAACwGAIgAACAxRAAAQAALIYACAAAYDEEQAAAAIshAAIAAFgMARAAAMBiCIAAAAAWQwAEAACwGAIgAACAxRAAAQAALIYACAAAYDEEQAAAAIshAAIAAFgMARAAAMBiCIAAAAAWQwAEAACwGAIgAACAxRAAAQAALIYACAAAYDEEQAAAAIshAAIAAFgMARAAAMBiCIAAAAAWQwAEAACwGAIgAACAxRAAAQAALIYACAAAYDEEQAAAAIshAAIAAFgMARAAAMBiCIAoNGw2m0aPHn3Dy40ePVo2my33C0KhZLPZ9MQTT9z08omJierWrZuCg4Nls9k0ffr03CvORS1YsEA2m03x8fHOLgXA/0cAhMvK+tK49FGqVCm1bt1ay5cvd3Z5yGWX/r6///77bPONMQoPD5fNZtM999yTp7X8+OOPGj16tE6dOpXrfQ8ZMkQrV67U8OHD9d5776l9+/a5vo5LnT17VqNGjVLNmjXl5+en4OBg1alTR0899ZQOHz5sb/f111/f1D9Qlxo/fryWLl16awXnEmOMgoKCNHfuXEnSzz//nGMIjY+Pz/Z3Juvx4YcfZut3586dat++vfz9/RUUFKR//etfOnbsWLZ2mZmZmjRpkiIjI+Xj46NatWpp0aJF2dq99dZbatmypUJCQuTt7a3IyEj17duXsIw85+HsAoBrGTt2rCIjI2WMUWJiohYsWKAOHTroyy+/dAgC58+fl4fHjb+lX3jhBQ0bNiw3S8Yt8PHx0cKFC3XHHXc4TP/222916NAheXt753kNP/74o8aMGaM+ffqoWLFiudr3//73P3Xu3FnPPPNMrvabk/T0dLVo0UJ//vmnYmJiNGjQIJ09e1a///67Fi5cqC5duqh06dKSLgbAWbNm3VIIHD9+vLp166b77rvPYfq//vUv9ejRI19+d1l2796tkydPqkmTJpKkDRs2KCQkROXLl8+xfc+ePdWhQweHaVFRUQ7PDx06pBYtWigwMFDjx4/X2bNnNXnyZP3666/avHmzvLy87G2ff/55vfLKK3rkkUfUsGFDff755+rVq5dsNpt69Ohhb/fzzz8rMjJS9957r4oXL659+/bprbfe0rJly7Rjxw777wfIbQRAuLy7775bDRo0sD/v37+/QkJCtGjRIocA6OPjc1P9e3h43FRwxI1LTk6Wn5/fVdt06NBBixcv1owZMxx+LwsXLlT9+vV1/PjxvC4zTx09ejRXQ2VKSoq8vLzk5pb9gM7SpUv1888/64MPPlCvXr2yLZeWlpZrdVyNu7u73N3d82VdWTZv3ix/f3/VrFlT0sUA2Lhx4yu2r1evnh566KGr9jl+/HglJydr27ZtKleunCSpUaNGatu2rRYsWKBHH31UkvT3339rypQpGjhwoGbOnClJGjBggFq2bKlnn31WDzzwgH17zJ49O9t67rvvPjVo0EDvvvsu/5wiz3AIGAVOsWLF5Ovrmy20XToG8Pz586pataqqVq2q8+fP29v8888/CgsLU9OmTZWRkSHp+scAli9fXn369Mk2vVWrVmrVqpXDtNdff101atRQkSJFVLx4cTVo0EALFy60z9+/f78ef/xxValSRb6+vgoODtYDDzyQ42GfX375RS1btpSvr6/Kli2rcePGKTY2NsfDWcuXL1fz5s3l5+enokWLqmPHjvr999+v+dqyDr9+9913+ve//63g4GAFBATo4Ycf1smTJ7O1v5719OnTR/7+/tq7d686dOigokWLqnfv3tespWfPnjpx4oRWrVpln5aWlqZPPvkkW4jJkpycrKefflrh4eHy9vZWlSpVNHnyZBljHNpljd9bunSpatasKW9vb9WoUUMrVqywtxk9erSeffZZSVJkZKT9cODl2/pqfeQkaxsbYzRr1ix7v1n++usvPfDAAwoKClKRIkXUpEkTffXVVw59rFu3zn5o8oUXXlCZMmVUpEgRnT59Osd17t27V5LUrFmzbPN8fHwUEBAg6eLvatasWfZtdHltkydPVtOmTRUcHCxfX1/Vr19fn3zySbZtm5ycrHfeece+fNbn5UpjAGfPnq0aNWrI29tbpUuX1sCBA7Mddm/VqpVq1qypP/74Q61bt1aRIkVUpkwZTZo0KdtrOnv2rI4fP67jx4/r+++/1+23366TJ0/q+PHj2rBhg6pXr67jx4/n+J6WLr6PrhaKP/30U91zzz328CdJ0dHRuu222/Txxx/bp33++edKT0/X448/7rB9HnvsMR06dEgbNmy44jok2fdS5sUQBMDOAC4qNjbWSDKrV682x44dM0ePHjW//fab+fe//23c3NzMN99849Bekhk1apT9+caNG427u7sZMmSIfVqPHj2Mr6+viYuLs08bNWqUuZ6PQkREhImJick2vWXLlqZly5b252+++aaRZLp162bmzp1rXnvtNdO/f3/z5JNP2tssXrzY1K5d24wcOdK8+eabZsSIEaZ48eImIiLCJCcn29sdOnTIBAUFmeDgYDNmzBgzefJkU7VqVVO7dm0jyezbt8/e9t133zU2m820b9/evP7662bixImmfPnyplixYg7tcpK1rW+//XbTvHlzM2PGDDNw4EDj5uZmWrRoYTIzM294PTExMcbb29tUrFjRxMTEmDfeeMO8++6716xhy5YtpmnTpuZf//qXfd7SpUuNm5ub+fvvv01ERITp2LGjfV5mZqZp06aNsdlsZsCAAWbmzJmmU6dORpIZPHiwwzokmdq1a5uwsDDz0ksvmenTp5sKFSqYIkWKmOPHjxtjjNmxY4fp2bOnkWSmTZtm3nvvPfPee++Zs2fPXncfOdm7d6957733jCTTtm1be7/GGJOQkGBCQkJM0aJFzfPPP2+mTp1qateubdzc3MySJUvsfaxdu9ZIMtWrVzd16tQxU6dONRMmTHB4z1xq4cKFRpIZO3asw+/wcj/++KNp27atkWSvK6s2Y4wpW7asefzxx83MmTPN1KlTTaNGjYwks2zZMnub9957z3h7e5vmzZvbl//xxx8dfreXvj+yPnfR0dHm9ddfN0888YRxd3c3DRs2NGlpafZ2LVu2NKVLlzbh4eHmqaeeMrNnzzZt2rQxkszXX3/t8DpiYmKMpGs+IiIi7Mvs27fPSDL+/v5GkrHZbKZBgwZm5cqVDn0fOnTISDITJ07Mtv0eeughExQUZH8+YMAA4+fnl22b79mzx0gyM2bMyNbH8ePHTWJiotmyZYv9/Xv53zggNxEA4bKy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w4cPl4+Pjz755BPFxMRoypQp9mVSUlKy9R8REaE9e/ZkW+fl07IOR5YqVUrR0dE3/Nqy7N69274nQrq4fY8cOWK/Plpured6dOnSRf/+97+1ceNGffTRR1dsFxERodWrV+vMmTMOewH//PNP+/wbld93homIiFBcXFy26bfyGq6mePHiqlixon777Tf7tCu95k8//VQ+Pj5auXKlw2HN2NjYbG2vd7tlvZ64uDiHz05aWpr27dt3U++thx9+WHfccYfOnTunzp0769VXX1WdOnX03XffaeLEifryyy9v6J+RrMPLWSGyTJkyKlmypLZu3Zqt7ebNmx2GadSpU0dvv/22du7cqerVq9unb9q0yT7/Ws6fP3/dh+iBm8EYQBQ46enp+uabb+Tl5WU/RHaldn369FHp0qX12muvacGCBUpMTNSQIUNuar0VK1bUxo0bHS4TsWzZMh08eNCh3YkTJxyee3l5qXr16jLGKD09XdLFPRqX74V4/fXX7ZemydKuXTtt2LBB27dvt0/7559/9MEHH2RrFxAQoPHjx9vXcamc7lSQkzfffNNh+Tlz5ujChQu6++67c3U918Pf319z5szR6NGj1alTpyu269ChgzIyMuzXW8sybdo02Ww2e+03Iutahfl1GY4OHTpo8+bNDpcHSU5O1ptvvqny5cs7hIgbsWPHjhyvm7h//3798ccfDodgr/Sa3d3dZbPZHN6b8fHxOd7xw8/P77q2WXR0tLy8vDRjxgyHz8G8efOUlJSkjh07XrOPy1WoUEHR0dEqWrSobDab+vfvr+joaKWlpalu3bq66667FB0dnS1c5vSe/fvvvzV//nzVqlXLvudPkrp27ZrtM79mzRrt2rVLDzzwgH1a586d5enp6XCNP2OM3njjDZUpU0ZNmzaVdPEf2ZyGDWzevFm//vqrw9AXILexBxAub/ny5fY9IUePHtXChQu1e/duDRs27Kpj27LGLK1Zs0ZFixZVrVq1NHLkSL3wwgvq1q1btqv+X8uAAQP0ySefqH379urevbv27t2r999/3+FkAEm66667FBoaqmbNmikkJEQ7d+7UzJkz1bFjR/seqnvuuUfvvfeeAgMDVb16dW3YsEGrV69WcHCwQ1///e9/9f7776tt27YaNGiQ/Pz89Pbbb6tcuXL6559/7HtcAgICNGfOHP3rX/9SvXr11KNH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/nF2iZIkHx8fTZw4USdPnnR2KbgMAbCAW7Fihe644w4VK1ZMwcHBuueee7R37177/Pj4eNlsNi1ZskStW7dWkSJFVLt2bW3YsCFf6sv6T/rSh7u7uz7//HPVq1dPPj4+qlChgsaMGaMLFy5Iknr16qUHH3zQoZ/09HSVKFFC7777bp7UWa9ePYWHh2vJkiX2aUuWLFG5cuVUt25d+7Tr3d4fffSRWrZsKR8fH33wwQe5Xu/Zs2f10Ucf6bHHHlPHjh21YMEC+7ys/+q/+uor1apVSz4+PmrSpIl+++03e5sFCxaoWLFi+uKLL1S9enV5e3vrwIEDuVZfbm3PNm3a6IknnnDo+9ixY/Ly8nK4i09eKF++vKZPn+4wrU6dOho9erT9uc1m09tvv60uXbqoSJEiqly5sr744os8retqrqfm/HS192nWe/BSOe0ZGjdunEqVKqWiRYtqwIABGjZsmOrUqZOrdZ46dUrr16/XxIkT1bp1a0VERKhRo0YaPny47r33XnubAQMGqGTJkgoICFCbNm20Y8cOex+jR49WnTp1NHfuXIWHh6tIkSLq3r27kpKScrXWLAMHDpSXl5e++eYbtWzZUuXKldPdd9+t1atX6++//9bzzz8vSUpNTdVzzz2n8PBweXt7q1KlSpo3b57i4+PVunVrSVLx4sXzbM98dHS0QkNDNWHChCu2+fTTT1WjRg15e3urfPnymjJlin3eiBEj1Lhx42zL1K5dW2PHjs31eq2EAFjAJScna+jQodq6davWrFkjNzc3denSJdvenOeff17PPPOMtm/frttuu009e/a0B678tn79ej388MN66qmn9Mcff2ju3LlasGCBXn75ZUlS79699eWXX+rs2bP2ZVauXKlz586pS5cueVZXv379FBsba38+f/78bHeFud7tPWzYMD311FPauXOn2rVrl+u1fvzxx6pataqqVKmihx56SPPnz9fll/R89tlnNWXKFG3ZskUlS5ZUp06dHPZGnjt3ThMnTtTbb7+t33//XaVKlcrVGnNjew4YMEALFy5UamqqfZn3339fZcqUUZs2bXK13ps1ZswYde/eXb/88os6dOig3r17u8zeF2e7nvfp1XzwwQd6+eWXNXHiRG3btk3lypXTnDlzcr1Of39/+fv7a+nSpQ7vtUs98MADOnr0qJYvX65t27apXr16uvPOOx1+13v27NHHH3+sL7/8UitWrNDPP/+sxx9/PNfr/eeff7Ry5Uo9/vjj8vX1dZgXGhqq3r1766OPPpIxRg8//LAWLVqkGTNmaOfOnZo7d678/f0VHh6uTz/9VJIUFxenI0eO6LXXXsv1Wt3d3TV+/Hi9/vrrOnToULb527ZtU/fu3dWjRw/9+uuvGj16tF588UX7Pwu9e/fW5s2bHf4x/P333/XLL7+oV69euV6vpRgUODExMaZz5845zjt27JiRZH799VdjjDH79u0zkszbb79tb/P7778bSWbnzp15Xqe7u7vx8/OzP7p162buvPNOM378eIe27733ngkLCzPGGJOenm5KlChh3n33Xfv8nj17mgcffDDP6uzcubM5evSo8fb2NvHx8SY+Pt74+PiYY8eOmc6dO5uYmJgcl73S9p4+fXqe1JqladOm9nVkba+1a9caY4xZu3atkWQ+/PBDe/sTJ04YX19f89FHHxljjImNjTWSzPbt23O9ttzcnufPnzfFixe3122MMbVq1TKjR4/O9bovrd0YYyIiIsy0adMc5teuXduMGjXK/lySeeGFF+zPz549aySZ5cuX50l9ObmZmj/77LN8qe1q79PY2FgTGBjo0P6zzz4zl34tNW7c2AwcONChTbNmzUzt2rVzvdZPPvnEFC9e3Pj4+JimTZua4cOHmx07dhhjjFm/fr0JCAgwKSkpDstUrFjRzJ071xhjzKhRo4y7u7s5dOiQff7y5cuNm5ubOXLkSK7WunHjxqv+HqdOnWokmU2bNhlJZtWqVTm2y/pbcfLkyVytL8ul780mTZqYfv36GWMcf8+9evUybdu2dVju2WefNdWrV7c/r127thk7dqz9+fDhw03jxo3zpGYrYQ9gAbd792717NlTFSpUUEBAgMqXLy9J2Q7n1apVy/5zWFiYJOno0aN5Xl/r1q21fft2+2PGjBnasWOHxo4da/+v29/fX4888oiOHDmic+fOycPDQ927d7cfOk1OTtbnn3+u3r1752mtJUuWtB+mio2NVceOHVWiRAmHNte7vRs0aJBndcbFxWnz5s3q2bOnJMnDw0MPPvig5s2b59AuKirK/nNQUJCqVKminTt32qd5eXk5vC9yW25sTx8fH/3rX//S/PnzJUk//fSTfvvtN5c6ieTSbejn56eAgIB8+Wy5uut9n16rj0aNGjlMu/x5bunatasOHz6sL774Qu3bt9e6detUr149LViwQDt27NDZs2cVHBzs8Hdr3759DnumypUrpzJlytifR0VFKTMzU3FxcXlSs7nG3tT4+Hi5u7urZcuWebL+GzFx4kS98847Dn+DJGnnzp1q1qyZw7RmzZpp9+7dysjIkHRxL+DChQslXXzNixYtyvPvAyuw9L2AC4NOnTopIiJCb731lkqXLq3MzEzVrFlTaWlpDu08PT3tP2eNscnNQf9X4ufnp0qVKjlMO3v2rMaMGaP7778/W3sfHx9JFz/wLVu21NGjR7Vq1Sr5+vqqffv2eV5vv3797GPOZs2alW3+9W5vPz+/PKtx3rx5unDhgkqXLm2fZoyRt7e3Zs6ced39+Pr65vmZeLmxPQcMGKA6dero0KFDio2NVZs2bRQREZGndUuSm5tbti/YnE7oufSzJV38fOXHZysn11tzfrjW+9SVas3i4+Ojtm3bqm3btnrxxRc1YMAAjRo1So8//rjCwsK0bt26bMtcPo4xP1SqVEk2m007d+7McVjMzp07Vbx48WyHh52pRYsWateunYYPH37D/8D17NlTzz33nH766SedP39eBw8ezDZOHDeOAFiAnThxQnFxcXrrrbfUvHlzSdL333/v5KqurV69eoqLi8sWDC/VtGlThYeH66OPPtLy5cv1wAMPZPuizQvt27dXWlqabDZbtrF7rrC9L1y4oHfffVdTpkzRXXfd5TDvvvvu06JFi1S1alVJ0saNG+1n3J48eVK7du1StWrV8rXe3Niet99+uxo0aKC33npLCxcuvKGQeytKliypI0eO2J+fPn1a+/bty5d13yxXqfl63qcRERE6c+aMkpOT7f8wbd++3aFtlSpVtGXLFj388MP2aVu2bMnz+rNUr15dS5cuVb169ZSQkCAPDw/7XuqcHDhwQIcPH7aH3o0bN8rNzU1VqlTJ1bqCg4PVtm1bzZ49W0OGDHEIegkJCfrggw/08MMP6/bbb1dmZqa+/fZbRUdHZ+vHy8tLkux72vLaK6+8ojp16jhsj2rVqmW71M4PP/yg2267Te7u7pKksmXLqmXLlvrggw90/vx5tW3bNtfHLFsRAbAAK168uIKDg/Xmm28qLCxMBw4c0LBhw5xd1jWNHDlS99xzj8qVK6du3brJzc1NO3bs0G+//aZx48bZ2/Xq1UtvvPGGdu3apbVr1+ZLbe7u7vZDFFl/fLK4wvZetmyZTp48qf79+yswMNBhXteuXTVv3jy9+uqrkqSxY8cqODhYISEhev7551WiRIl8v15cbm3PAQMG6IknnpCfn1+engh0qTZt2mjBggXq1KmTihUrppEjR2Z7Da7GVWq+nvfpypUrVaRIEY0YMUJPPvmkNm3a5HCWsCQNGjRIjzzyiBo0aKCmTZvqo48+0i+//KIKFSrkar0nTpzQAw88oH79+qlWrVoqWrSotm7dqkmTJqlz586Kjo5WVFSU7rvvPk2aNEm33XabDh8+rK+++kpdunSxD/nw8fFRTEyMJk+erNOnT+vJJ59U9+7dFRoamqv1StLMmTPVtGlTtWvXTuPGjVNkZKR+//13PfvssypTpoxefvllBQUFKSYmRv369dOMGTNUu3Zt7d+/X0ePHlX37t0VEREhm82mZcuWqUOHDvL19ZW/v3+u15rl9ttvV+/evTVjxgz7tKeffloNGzbUSy+9pAcffFAbNmzQzJkzNXv2bIdle/furVGjRiktLU3Tpk3LsxqthDGABVBmZqY8PDzk5uamDz/8UNu2bVPNmjU1ZMgQ+5e/K2vXrp2WLVumb775Rg0bNlSTJk00bdq0bIf1evfurT/++ENlypTJNkYkLwUEBCggICDbdFfY3vPmzVN0dHS2L1Xp4hfr1q1b9csvv0i6+N/2U089pfr16yshIUFffvml/T/+/JQb27Nnz57y8PBQz5497cME8kLWZ0uShg8frpYtW+qee+5Rx44ddd9996lixYp5tu6b5Yo1X8/79NChQ3r//ff19ddf6/bbb9eiRYuyXa6md+/eGj58uJ555hnVq1dP+/btU58+fXL9PeDv76/GjRtr2rRpatGihWrWrKkXX3xRjzzyiGbOnCmbzaavv/5aLVq0UN++fXXbbbepR48e2r9/v0JCQuz9VKpUSffff786dOigu+66S7Vq1coWZHJL5cqVtXXrVlWoUEHdu3dXxYoV9eijj6p169basGGDgoKCJElz5sxRt27d9Pjjj6tq1ap65JFHlJycLEkqU6aMxowZo2HDhikkJCTbJZfywtixYx2GSNSrV08ff/yxPvzwQ9WsWVMjR47U2LFjsx0m7tatm06cOKFz58659IXPCxKbudYoUric9u3bq1KlSvl2KAwFy7p169S6dWudPHnSKeOT8kJ8fLwqVqyoLVu2qF69enm2noL42SqINd+Ktm3bKjQ0VO+9956zS3EwevRoLV26NNthbMBVcQi4ADl58qR++OEHrVu3LsfbbAGFTXp6uk6cOKEXXnhBTZo0ybPwVxA/WwWx5ht17tw5vfHGG2rXrp3c3d21aNEirV69WqtWrXJ2aUCBRwAsQPr166ctW7bo6aefVufOnZ1dDpDnfvjhB7Vu3Vq33XabPvnkkzxbT0H8bBXEmm9U1qHXl19+WSkpKapSpYo+/fTTHE9oAHBjOAQMAABgMZwEAgAAYDEEQAAAAIshAAIAAFgMARAAAMBiCIAAAAAWQwAE4HJatWolm81mf7i7u6tMmTLq1KmTfvzxxzxd97p16+zrvfzWZFl1Xe1+sABQEBAAAbgsLy8vNW7cWLVq1dLRo0e1bNkytWzZUps3b77lvtPS0nKhwvxRkGoFUDAQAAG4rLCwMG3cuFE///yzli5dKkm6cOGCFi5caG+zfPlytWzZUkWLFpWvr6+aN2+utWvX2ufHx8fb9+i9/fbbuvPOO+Xj46Px48fnWp2xsbGqX7++fH195efnp2bNmunzzz/PsYZ169bZp5cvX142m81+D9xL9z4uXrxYjRo1kpeXl8PrBYDcQAAEUGB99NFH6tixo7777jsFBwcrLCxM33//vdq2besQArMMHDhQO3bsUMWKFeXu7p4rNYwbN079+vXTTz/9pFKlSikgIEA//vij7rvvPr3//vs33e9DDz2kQ4cOKTIyUjabLVdqBYAsBEAALuvIkSNq0qSJ6tatq/vuu0+S5OHhoZ49e0qShg0bJmOM+vXrp3379mnv3r3q0qWLMjIyNHLkyGz9RUVF6dChQ/r99981YsSIa66/b9++DmMRv/32W4f5ycnJ9j2JXbp00b59+xQfH69GjRpJkl544YWbfu3dunXToUOHFBcXp4ceeuim+wGAnHAvYAAuKy0tTZs2bZKbm5tCQkJUr149jRgxQo0bN9axY8cUHx8vSZo/f77mz5/vsOymTZuy9fef//xHPj4+knRdewArVKigkiVL2p//8ccfOnPmjP3577//rvPnz0uSevToITc3N3l7e6tr167avHmz9u/fr2PHjt3w65akQYMGyc3N7bprBYAbQQAE4LIiIiLsIe9qLg9qWS4/eSIkJOSG1v/iiy+qT58+9uetWrXKthfwelx6CDcjI8P+c1JS0hWXudFaAeBGEAABFEglS5ZURESE9u/fr3r16mnRokXy8Lj4J23Xrl3av3+/vLy8HJbJ7bF0NWrUkK+vr86fP6+PPvpI3bp1U3p6upYsWSLpYoAtWbKkfS9hVm133nmnVq9erVOnTl2xb8b9AchLjAEEUGBljb/75JNPVLp0adWtW1ehoaGqUqWKPvjggzxfv5+fn30s4ZIlSxQZGany5cvbDz+PGzdOkuTr66uoqChJ0tNPP602bdqoc+fO9kO8AJDf+OsDoMDq1auX/dqA58+fV1xcnIoWLaqHH35YAwYMyJcaXnjhBc2bN0/16tXT0aNHlZSUpKioKC1dutTh5I0FCxaoefPmkqRDhw5p9uzZCg8Pz5caAeByNmOMcXYRAAAAyD/sAQQAALAYAiAAAIDFEAABAAAshgAIAABgMQRAAAAAiyEAAgAAWAwBEAAAwGIIgAAAABZDAAQAALAYAiAAAIDFEAABAAAshgAIAABgMf8P0C+gsUT42qEAAAAASUVORK5CYII="
    private val TAG = "Monthly Station Statistics"
    var myTableLayout:TableLayout? = null
    var jObj: MonthlyStatisticStation? = null
    var tabrow:TableRow? =null
    val tabrow0: TextView? = null
    val tabrow1: TextView? = null
    val tabrow2: TextView? = null
    val tabrow3: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_station_statistic)
        val codas = intent.getStringExtra("Code")?.toInt()
        val tempas = intent.getStringExtra("Temps")
        val namas = intent.getStringExtra("Name")
        val annas = intent.getStringExtra("Annee")?.toInt()
       // station = intent.getSerializableExtra("data") as Station

       Station_code.text = codas.toString()
       Station_name.text = namas

        if (tempas != null) {
            temps = tempas
        }
        if (codas != null) {
            code = codas
        }
        if (annas != null) {
            annee = annas
        }
        myTableLayout = findViewById(R.id.table_main)
        myImage = findViewById(R.id.image)
        //IpAddressDialog.ipAddressInput?.let { requestToServer(it) }
        IpAddressDialog.ipAddressInput?.let { requestToServer(it) }
       // image.setImageBitmap(Base64Util.convertStringToBitmap(myImageString))
        val imageByte = Base64.decode(myImageString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
        image.setImageBitmap(decodedImage)
    }


    private fun requestToServer(ipAddress: String) {

        // get check connexion with Server Hello from Server
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$ipAddress/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient().build())
            .build()
        val service: WebBixiService = retrofit.create(WebBixiService::class.java)
        val call: Call<String> = service.getStationStatistics(annee, temps, code)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                Log.i(TAG, "Réponse des Statistiques du Serveur: ${response?.body()}")
                Log.i(TAG, "Status de reponse  des Statistiques du Serveur: ${response?.code()}")
                Log.i(TAG, "Message de reponse  des Statistiques du Serveur: ${response?.message()}")

                val arrayStationType = object : TypeToken<MonthlyStatisticStation>() {}.type
                jObj = Gson().fromJson(response?.body(), arrayStationType)
                Log.i(TAG, "L'objet : $jObj")
                fillmyTablelayout(jObj)
                //Log.i(TAG, "L'objet : $jObj")
            }
            override fun onFailure(call: Call<String>?, t: Throwable) {
                Log.i(TAG, "Echec de connexion avec le serveur !!!")
            }
        })
    }
    fun fillmyTablelayout(listStation: MonthlyStatisticStation?) {

        var count = 1
        var stringTemp:String? = null
        var stringdeparture:String? = null
        var stringArrival:String? = null
        // Table Headers
        Log.i(TAG, "ligne 113 !!")
        myImageString = listStation!!.graphique

        Log.i(TAG, "$listStation !!")
        Log.i(TAG, "$myImageString !!")

        for (item in 0 .. listStation.donnees.time.size) {
            stringTemp = listStation.donnees.time[item]
            stringdeparture = listStation.donnees.departureValue[item].toString()
            stringArrival = listStation.donnees.arrivalValue[item].toString()

            Log.i(TAG, "valeur tabrownwnddn!!")


            tabrow0?.setText(count.toString())
            tabrow0?.setTextColor(Color.BLACK)
            tabrow?.addView(tabrow0)
            Log.i(TAG, "valeur de tabrow0 $tabrow0 !!!")


           // val tabrow1: TextView? = null
            if (tabrow1 != null) {
                tabrow1.setText(stringTemp)
            }
            if (tabrow1 != null) {
                tabrow1.setTextColor(Color.BLACK)
            }
            tabrow?.addView(tabrow1)

            //val tabrow2: TextView? = null
            if (tabrow2 != null) {
                tabrow2.setText(stringdeparture)
            }
            if (tabrow2 != null) {
                tabrow2.setTextColor(Color.BLACK)
            }
            tabrow?.addView(tabrow2)

           // val tabrow3: TextView? = null
            if (tabrow3 != null) {
                tabrow3.setText(stringArrival)
            }
            if (tabrow3 != null) {
                tabrow3.setTextColor(Color.BLACK)
            }
            tabrow?.addView(tabrow3)
            Log.i(TAG, "valeur de tabrow0 $tabrow0 !!!")
            Log.i(TAG, "valeur tabrow1 $tabrow1 !!")

            myTableLayout?.addView(tabrow)
            count +=1
        }
    }


    object Base64Util {
        private val IMG_WIDTH = 640
        private val IMG_HEIGHT = 480
        private fun resizeBase64Image(base64image: String): String {
            val encodeByte: ByteArray = Base64.decode(base64image.toByteArray(), Base64.DEFAULT)
            val options = BitmapFactory.Options()
            var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)

            image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false)
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            System.gc()
            return Base64.encodeToString(b, Base64.NO_WRAP)
        }

        private fun convertString64ToImage(base64String: String): Bitmap {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        fun convertStringToBitmap(base64String: String): Bitmap {
            return convertString64ToImage(resizeBase64Image(base64String))
        }
    }


}